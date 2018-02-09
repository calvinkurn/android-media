package com.tokopedia.core.util;

import android.app.Activity;

import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.UpdateManager;

/**
 * Created by ricoharisin on 9/19/16.
 */
public class HockeyAppHelper {
    public static final String KEY_MAINAPP = "a2ba10bfc3ec8f60d1c6d7c9ac27fe4a";
    public static final String KEY_SELLERAPP = "404b7bc37aeb4c5bbf0c033c1a9e1f2e";

    private static Boolean ENABLE_DISTRIBUTION = false;
    private static Boolean ANONYMOUS_LOGIN = false;
    private static String HOCKEYAPP_KEY = KEY_MAINAPP;

    private static final String HOCKEYAPP_DOWNLOAD_URL = "https://rink.hockeyapp.net/apps/528b17702bf941a581f15e188677306b";

    public static void handleLogin(Activity activity) {
        if (isAllow() && !ANONYMOUS_LOGIN) {
            LoginManager.register(activity, HOCKEYAPP_KEY, LoginManager.LOGIN_MODE_EMAIL_PASSWORD);
            LoginManager.verifyLogin(activity, activity.getIntent());
        }
    }

    public static void checkForUpdate(Activity activity) {
        if (isAllow()) UpdateManager.register(activity, HOCKEYAPP_KEY);
    }

    public static void unregisterManager() {
        if (isAllow()) UpdateManager.unregister();
    }

    private static Boolean isAllow() {
        return ENABLE_DISTRIBUTION;
    }

    public static void setEnableDistribution(Boolean enableDistribution) {
        ENABLE_DISTRIBUTION = enableDistribution;
    }

    public static void setAllowAnonymousLogin(Boolean isAllow) {
        ANONYMOUS_LOGIN = isAllow;
    }

    public static Boolean getEnableDistribution() {
        return ENABLE_DISTRIBUTION;
    }

    public static void setHockeyappKey(String key) {
        HOCKEYAPP_KEY = key;
    }

    public static String getHockeyappDownloadUrl() {
        return HOCKEYAPP_DOWNLOAD_URL;
    }
}
