package com.tokopedia.gamification.cracktoken.util;

/**
 * Created by hendry on 10/04/18.
 */

public class TokenMarginUtil {
    private static final double RATIO_IMAGE_WIDTH = 0.5;
    private static final double RATIO_IMAGE_MARGIN_BOTTOM = 0.64;
    public static final double RATIO_IMAGE_MARGIN_TOP = 0.795;
    private static final double RATIO_TIMER_MARGIN_BOTTOM = 0.75;
    public static final float STAGE_PIXEL = 1670;


    public static int getEggWidth(int rootWidth, int rootHeight) {
        return (int) (RATIO_IMAGE_WIDTH * Math.min(rootWidth, rootHeight));
    }

    public static int getEggMarginBottom(int rootHeight) {
        return (int) (RATIO_IMAGE_MARGIN_BOTTOM * (rootHeight));
    }

    public static int getTimerMarginBottom(int rootHeight) {
        return (int) (RATIO_TIMER_MARGIN_BOTTOM * (rootHeight));
    }
}
