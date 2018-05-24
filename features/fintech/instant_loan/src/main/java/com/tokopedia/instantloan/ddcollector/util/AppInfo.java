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

    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(context), 0);
            return pInfo.versionName + "";
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Exception", "Error while retrieving app version name ", e);
        }

        return NOT_AVAILABLE;
    }

    public static String getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(context), 0);
            return pInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Exception", "Error while retrieving app version code ", e);
        }

        return NOT_AVAILABLE;
    }

    public static String getApplicationName(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }

        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : NOT_AVAILABLE);
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
