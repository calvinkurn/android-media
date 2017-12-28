package com.tokopedia.core.onboarding;

import android.graphics.Color;
import android.view.View;

/**
 * Created by stevenfredian on 7/26/17.
 */

public class ColorTransformer extends BasePageTransformer {


    private final Integer[] list;

    public ColorTransformer(Integer[] list) {
        this.list = list;
    }

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public void transformPage(final View page, final int pageIndex, final float position) {

        if (inRange(position)) { // [-1, 1]
            if (isRightPage(position)) { //(0, 1]

                final int leftIndex = pageIndex - 1;
                final int rightIndex = pageIndex;

                final int leftColor = list[leftIndex];
                final int rightColor = list[rightIndex];

                final int composedColor = blendColors(leftColor, rightColor, position);
                page.setBackgroundColor(composedColor);

            } else if (isLeftPage(position)) { //[-1, 0)

                final int leftIndex = pageIndex;
                final int rightIndex = leftIndex + 1;

                final int leftColor = list[leftIndex];
                final int rightColor = list[rightIndex];

                final int composedColor = blendColors(leftColor, rightColor, 1 - Math.abs(position));
                page.setBackgroundColor(composedColor);

            } else { // position == 0
                page.setBackgroundColor(list[pageIndex]);
            }
        } else { //(-Infinity, -1) or (1, + Infinity)
            page.setBackgroundColor(list[pageIndex]);
        }
    }
}
