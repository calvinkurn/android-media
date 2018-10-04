package com.tokopedia.instantloan.ddcollector.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Locale;

import static com.tokopedia.instantloan.ddcollector.DDConstants.NOT_AVAILABLE;


/**
 * Utility class for fetching the application information
 */
public class AppInfo {

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getDefaultAcceptLanguage(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration == null) {
            return null;
        }

        Locale locale = configuration.locale;
        if (locale == null) {
            return null;
        }

        return locale.toString().replace('_', '-');
    }
}
