package com.tokopedia.gamification.cracktoken.util;

/**
 * Created by hendry on 10/04/18.
 */

public class TokenMarginUtil {
    private static final double RATIO_IMAGE_WIDTH = 0.5;
    public static final double RATIO_IMAGE_MARGIN_TOP = 0.795;
    public static final float STAGE_PIXEL = 1670f;
    public static final float BASE_DRAWABLE_HEIGHT = 2701f;


    public static int getEggWidth(int rootWidth, int rootHeight) {
        return (int) (RATIO_IMAGE_WIDTH * Math.min(rootWidth, rootHeight));
    }

}
