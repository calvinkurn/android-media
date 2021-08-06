package com.tokopedia.home.testenv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.coachmark.CoachMark2.Companion.isCoachmmarkShowAllowed
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.NAVIGATION_EXP_TOP_NAV
import com.tokopedia.searchbar.navigation_component.NavConstant.KEY_FIRST_VIEW_NAVIGATION
import com.tokopedia.searchbar.navigation_component.NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING
import com.tokopedia.searchbar.navigation_component.NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1
import com.tokopedia.searchbar.navigation_component.NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2
import com.tokopedia.searchbar.navigation_component.NavConstant.KEY_P1_DONE_AS_NON_LOGIN
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class HomeTestSetupActivity : AppCompatActivity(){
    companion object {
        private const val URI_HOME_MACROBENCHMARK = "macrobenchmark"
    }

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
        RemoteConfigInstance.getInstance().abTestPlatform.setString(NAVIGATION_EXP_TOP_NAV, NAVIGATION_EXP_TOP_NAV)
    }
}