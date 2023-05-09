package com.tokopedia.macrobenchmark_util.env.baselineprofile

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.tokopedia.coachmark.CoachMark2.Companion.isCoachmmarkShowAllowed
import com.tokopedia.macrobenchmark_util.env.SessionSetupActivity
import com.tokopedia.macrobenchmark_util.env.session.MacrobenchmarkAuthHelper
import com.tokopedia.user.session.UserSession

object BaselineProfileUtil {

    const val KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION"
    const val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING"
    const val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1 = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1"
    const val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2 = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2"
    const val KEY_P1_DONE_AS_NON_LOGIN = "KEY_P1_DONE_AS_NON_LOGIN"

    fun handleBaselineProfileSession(context: Context) {
        val baselineProfileDebugCache = "RemoteConfigDebugCache"

        isCoachmmarkShowAllowed = false
        val userSession = UserSession(context)
        userSession.setFirstTimeUserOnboarding(false)

        MacrobenchmarkAuthHelper.loginInstrumentationTestUser1(context)

        val sharedPrefs =
            context.getSharedPreferences(baselineProfileDebugCache, MODE_PRIVATE)

        isCoachmmarkShowAllowed = false

        sharedPrefs.edit().putString("android_user_two_factor_check", "false").apply()

        val sharedPrefsHome = context.getSharedPreferences(
            KEY_FIRST_VIEW_NAVIGATION, MODE_PRIVATE
        )

        sharedPrefsHome.edit().putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false)
            .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, false)
            .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, false)
            .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, true).apply()
    }
}
