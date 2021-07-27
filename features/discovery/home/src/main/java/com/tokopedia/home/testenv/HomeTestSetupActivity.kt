package com.tokopedia.home.testenv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.coachmark.CoachMark2.Companion.isCoachmmarkShowAllowed
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class HomeTestSetupActivity : AppCompatActivity(){
    var KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION"
    var KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING"
    var KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1 = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1"
    var KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2 = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2"
    var KEY_P1_DONE_AS_NON_LOGIN = "KEY_P1_DONE_AS_NON_LOGIN"
    val NAVIGATION_EXP_TOP_NAV = "new_glmenu"

    var EXP_TOP_NAV: String = NAVIGATION_EXP_TOP_NAV
    var VARIANT_REVAMP: String = NAVIGATION_EXP_TOP_NAV
    val URI_HOME_MACROBENCHMARK = "macrobenchmark"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            val uri = intent.data
            uri?.let {
                if (uri.pathSegments[1] == URI_HOME_MACROBENCHMARK) {
                    handleHomeMacrobenchmarkUri(UserSession(this))
                }
            }
        }
    }

    private fun handleHomeMacrobenchmarkUri(userSession: UserSessionInterface) {
        isCoachmmarkShowAllowed = false
        val sharedPrefs = getSharedPreferences(
            KEY_FIRST_VIEW_NAVIGATION, MODE_PRIVATE
        )
        sharedPrefs.edit().putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false)
            .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, false)
            .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, false)
            .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, true).apply()
        userSession.setFirstTimeUserOnboarding(false)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(EXP_TOP_NAV, VARIANT_REVAMP)
    }
}