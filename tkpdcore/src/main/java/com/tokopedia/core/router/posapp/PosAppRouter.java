package com.tokopedia.core.router.posapp;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by okasurya on 8/14/17.
 */

public class PosAppRouter {
    public static final String IS_LOGOUT = "isLogout";

    private static String SPLASH_SCREEN_ACTIVITY = "com.tokopedia.posapp.PosAppSplashScreen";

    public static Intent getSplashScreenIntent(Context context, boolean isLogout) {
        Intent intent = RouterUtils.getActivityIntent(context, SPLASH_SCREEN_ACTIVITY);
        if(isLogout) {
            intent.putExtra(IS_LOGOUT, true);
        }
        return intent;
    }
}
