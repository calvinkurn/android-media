package com.tokopedia.gamification.cracktoken.util;

/**
 * Created by hendry on 10/04/18.
 */

public class TokenMarginUtil {
    public static final double RATIO_IMAGE_WIDTH = 0.5;
    public static final double RATIO_IMAGE_MARGIN_BOTTOM = 0.64;

    public static int getEggWidth(int rootWidth, int rootHeight) {
        return (int) (RATIO_IMAGE_WIDTH * Math.min(rootWidth, rootHeight));
    }

    public static int getEggMarginBottom(int rootHeight) {
        return (int) (RATIO_IMAGE_MARGIN_BOTTOM * (rootHeight));
    }
}
