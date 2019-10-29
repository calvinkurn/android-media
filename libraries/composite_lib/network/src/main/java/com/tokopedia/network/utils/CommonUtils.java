package com.tokopedia.network.utils;

import android.content.Context;
import android.os.Build;

/**
 * @author ricoharisin .
 */

public class CommonUtils {

    public static boolean isTimezoneNotAutomatic(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return android.provider.Settings.Global.getInt(
                    context.getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME, 0) == 0;
        } else {
            return android.provider.Settings.System.getInt(
                    context.getContentResolver(),
                    android.provider.Settings.System.AUTO_TIME, 0) == 0;
        }
    }
}
