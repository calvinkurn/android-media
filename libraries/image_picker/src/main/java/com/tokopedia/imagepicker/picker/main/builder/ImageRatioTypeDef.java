package com.tokopedia.imagepicker.picker.main.builder;

/**
 * Created by hendry on 04/05/18.
 */

public enum ImageRatioTypeDef {
    ORIGINAL(new int[]{-1, -1}),
    RATIO_1_1(new int[]{1, 1}),
    RATIO_3_4(new int[]{3, 4}),
    RATIO_4_3(new int[]{4, 3}),
    RATIO_16_9(new int[]{16, 9}),
    RATIO_9_16(new int[]{9, 16});

    int[] ratio;

    ImageRatioTypeDef(int[] ratio) {
        this.ratio = ratio;
    }

    public int[] getRatio() {
        return ratio;
    }

    public int getRatioX() {
        return ratio[0];
    }

    public int getRatioY() {
        return ratio[1];
    }
}
