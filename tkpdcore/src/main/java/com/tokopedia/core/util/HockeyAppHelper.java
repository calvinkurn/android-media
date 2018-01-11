package com.tokopedia.core.util;

import android.app.Activity;

import com.tokopedia.core.BuildConfig;

import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.UpdateManager;

/**
 * Created by ricoharisin on 9/19/16.
 */
public class HockeyAppHelper {

    private static Boolean ENABLE_DISTRIBUTION = false;
    private static final String HOCKEYAPP_DOWNLOAD_URL = "https://rink.hockeyapp.net/apps/528b17702bf941a581f15e188677306b";

    public static void handleLogin(Activity activity) {
        if (isAllow()) {
            LoginManager.register(activity, "a2ba10bfc3ec8f60d1c6d7c9ac27fe4a", LoginManager.LOGIN_MODE_EMAIL_ONLY);
            LoginManager.verifyLogin(activity, activity.getIntent());
        }
    }

    public static void checkForUpdate(Activity activity) {
        if (isAllow()) UpdateManager.register(activity);
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

    public static Boolean getEnableDistribution() {
        return ENABLE_DISTRIBUTION;
    }

    public static String getHockeyappDownloadUrl() {
        return HOCKEYAPP_DOWNLOAD_URL;
    }
}
