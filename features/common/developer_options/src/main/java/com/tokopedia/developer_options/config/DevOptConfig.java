package com.tokopedia.developer_options.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.tokopedia.coachmark.CoachMark2;
import com.tokopedia.user.session.UserSession;

public class DevOptConfig {

    public static final String CHUCK_ENABLED = "CHUCK_ENABLED";
    public static final String IS_CHUCK_ENABLED = "is_enable";

    public static final String KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION";
    public static final String KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING";
    public static final String KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1 = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1";
    public static final String KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2 = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2";
    public static final String KEY_P1_DONE_AS_NON_LOGIN = "KEY_P1_DONE_AS_NON_LOGIN";

    public static boolean isChuckNotifEnabled(Context context) {
        SharedPreferences cache = context.getSharedPreferences(CHUCK_ENABLED, Context.MODE_PRIVATE);
        return cache.getBoolean(IS_CHUCK_ENABLED, false);
    }

    public static void handleHomeMacrobenchmarkUri(Context context) {
        CoachMark2.Companion.setCoachmmarkShowAllowed(false);

        SharedPreferences sharedPrefs = context.getSharedPreferences(
                KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false)
                .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, false)
                .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, false)
                .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, true).apply();

        UserSession userSession = new UserSession(context);
        userSession.setFirstTimeUserOnboarding(false);
    }
}
