package com.tokopedia.tkpd.util;

import android.app.Activity;

import com.tokopedia.tkpd.BuildConfig;

import net.hockeyapp.android.LoginManager;
import net.hockeyapp.android.UpdateManager;

/**
 * Created by ricoharisin on 9/19/16.
 */
public class HockeyAppHelper {

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
        return BuildConfig.ENABLE_DISTRIBUTION;
    }
}
