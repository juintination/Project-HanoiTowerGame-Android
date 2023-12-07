package com.cookandroid.hanoitowerproject;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout space1, space2, space3;
    private LinearLayout pole1, pole2, pole3;
    private LinearLayout layout1, layout2, layout3;
    private View selectedDisk;
    private LinearLayout selectedSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        space1 = findViewById(R.id.space1);
        space2 = findViewById(R.id.space2);
        space3 = findViewById(R.id.space3);

        pole1 = findViewById(R.id.pole1);
        pole2 = findViewById(R.id.pole2);
        pole3 = findViewById(R.id.pole3);

        Button resetButton = findViewById(R.id.btnReset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDisk();
            }
        });

        layout1 = findViewById(R.id.layout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLayoutClick(space1);
            }
        });

        layout2 = findViewById(R.id.layout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLayoutClick(space2);
            }
        });

        layout3 = findViewById(R.id.layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLayoutClick(space3);
            }
        });

        startGame();
    }

    private void startGame() {
        stackDisk(space1, 3);
        stackDisk(space1, 2);
        stackDisk(space1, 1);

        Toast.makeText(this, "하노이탑 게임을 시작합니다!", Toast.LENGTH_SHORT).show();
    }

    private void initDisk() {
        space1.removeAllViews();
        space2.removeAllViews();
        space3.removeAllViews();

        stackDisk(space1, 3);
        stackDisk(space1, 2);
        stackDisk(space1, 1);

        selectedDisk = null;
        selectedSpace = null;

        updatePoleHeight();

        Toast.makeText(this, "원판 위치가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void stackDisk(LinearLayout space, int diskNum) {
        LinearLayout disk = new LinearLayout(this);
        int diskHeight = getResources().getDimensionPixelSize(R.dimen.disk_height);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, diskHeight
        );

        int marginDimen = getResources().getIdentifier("disk_margin_" + diskNum, "dimen", getPackageName());
        int margin = getResources().getDimensionPixelSize(marginDimen);
        layoutParams.setMargins(margin, 0, margin, 0);

        disk.setBackgroundColor(getResources().getColor(R.color.disk_color));
        space.addView(disk, 0, layoutParams);
        updatePoleHeight();
    }

    private void updatePoleHeight() {
        int poleHeight = getResources().getDimensionPixelSize(R.dimen.pole_height);
        int diskHeight = getResources().getDimensionPixelSize(R.dimen.disk_height);

        int pole1Height = poleHeight - (space1.getChildCount() * diskHeight);
        int pole2Height = poleHeight - (space2.getChildCount() * diskHeight);
        int pole3Height = poleHeight - (space3.getChildCount() * diskHeight);

        setPoleHeight(pole1, pole1Height);
        setPoleHeight(pole2, pole2Height);
        setPoleHeight(pole3, pole3Height);

        if (space3.getChildCount() == 3) {
            Toast.makeText(this, "게임이 종료되었습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPoleHeight(LinearLayout pole, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.pole_width), height
        );
        layoutParams.gravity = Gravity.CENTER;
        pole.setLayoutParams(layoutParams);
    }

    private void handleLayoutClick(LinearLayout space) {
        if (selectedDisk == null) {
            selectTopDisk(space);
        } else {
            moveSelectedDiskToSpace(space);
        }
    }

    private void selectTopDisk(LinearLayout space) {
        if (space.getChildCount() > 0) {
            selectedDisk = space.getChildAt(0);
            selectedSpace = space;
        }
    }

    private void moveSelectedDiskToSpace(LinearLayout destinationSpace) {
        if (selectedDisk != null && selectedSpace != null) {
            int selectedDiskIndex = selectedSpace.indexOfChild(selectedDisk);

            if (destinationSpace.getChildCount() == 0 || isDiskSmallerThanTop(destinationSpace, selectedDiskIndex)) {
                selectedSpace.removeView(selectedDisk);
                destinationSpace.addView(selectedDisk, 0);

                updatePoleHeight();
            } else {
                Toast.makeText(this, "큰 원판은 작은 원판 위에 놓을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
            selectedDisk = null;
            selectedSpace = null;
        }
    }

    private boolean isDiskSmallerThanTop(LinearLayout space, int diskIndex) {
        if (space.getChildCount() == 0 || diskIndex >= space.getChildCount()) {
            return true;
        }

        View topDisk = space.getChildAt(0);
        LinearLayout.LayoutParams topDiskParams = (LinearLayout.LayoutParams) topDisk.getLayoutParams();
        LinearLayout.LayoutParams selectedDiskParams = (LinearLayout.LayoutParams) selectedDisk.getLayoutParams();

        return selectedDiskParams.leftMargin >= topDiskParams.leftMargin && selectedDiskParams.rightMargin >= topDiskParams.rightMargin;
    }
}

