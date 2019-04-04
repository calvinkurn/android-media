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
    private static final int DEFAULT_STATUS_BAR_HEIGHT = 24;
    private static final String STATUS_BAR_HEIGHT_ID = "status_bar_height";

    /**
     * Get screen density information of the display
     *
     * @param context context where the method is called
     * @return String screen density in dpi format ("hdpi", "xhdpi", etc)
     */
    public static String getScreenDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (metrics.densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            return MDPI;
        } else if (metrics.densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return HDPI;
        } else if (metrics.densityDpi <= DisplayMetrics.DENSITY_XHIGH) {
            return XHDPI;
        } else if (metrics.densityDpi <= DisplayMetrics.DENSITY_XXHIGH) {
            return XXHDPI;
        } else if (metrics.densityDpi <= DisplayMetrics.DENSITY_XXXHIGH) {
            return XXXHDPI;
        } else {
            return XXXHDPI;
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = (int) (DEFAULT_STATUS_BAR_HEIGHT * context.getResources().getDisplayMetrics().density + 0.5f);
        int resourceId = context.getResources().getIdentifier(STATUS_BAR_HEIGHT_ID, "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
