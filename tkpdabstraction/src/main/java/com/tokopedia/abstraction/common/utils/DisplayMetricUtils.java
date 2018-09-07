package com.tokopedia.abstraction.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Utility class that consists of common methods to get general information about a display,
 * such as size, density, and font scaling.
 *
 * @author Aghny A. Putra on 29/01/18
 */
public class DisplayMetricUtils {

    public static final String LDPI = "ldpi";
    public static final String MDPI = "mdpi";
    public static final String HDPI = "hdpi";
    public static final String XHDPI = "xhdpi";
    public static final String XXHDPI = "xxhdpi";
    public static final String XXXHDPI = "xxxhdpi";

    /**
     * Get screen density information of the display
     *
     * @param context context where the method is called
     * @return String screen density in dpi format ("hdpi", "xhdpi", etc)
     */
    public static String getScreenDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_MEDIUM:
                return MDPI;
            case DisplayMetrics.DENSITY_HIGH:
                return HDPI;
            case DisplayMetrics.DENSITY_XHIGH:
                return XHDPI;
            case DisplayMetrics.DENSITY_XXHIGH:
                return XXHDPI;
            case DisplayMetrics.DENSITY_XXXHIGH:
                return XXXHDPI;
            default:
                return MDPI;
        }
    }

}
