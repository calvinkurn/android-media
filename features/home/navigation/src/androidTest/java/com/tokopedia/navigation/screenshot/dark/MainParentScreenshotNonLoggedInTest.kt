package com.tokopedia.navigation.screenshot.dark

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.navigation.com.tokopedia.navigation.helper.NavigationInstrumentationHelper.disableCoachMark
import com.tokopedia.navigation.com.tokopedia.navigation.mock.MainHomeMockResponseConfig
import com.tokopedia.navigation.com.tokopedia.navigation.screenshot.MainParentScreenshotTestHelper.turnOffAnimation
import com.tokopedia.navigation.presentation.activity.MainParentActivity
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 12/04/21.
 */
class MainParentScreenshotNonLoggedInTest {
    val TAG = "MainParentScreenshotTest"
    val KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION"
    val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING"
    val CHOOSE_ADDRESS_ROLLENCE_KEY = "hyperlocal_android"

    @get:Rule
    var activityRule = object: ActivityTestRule<MainParentActivity>(MainParentActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(MainHomeMockResponseConfig())
            setupDarkModeTest(true)
            setupHomeEnvironment()
            setupAbTestRemoteConfig()
            disableCoachMark(context)
            InstrumentationAuthHelper.clearUserSession()
        }
    }

    private fun setupHomeEnvironment() {
        val userSession = UserSession(context)
        userSession.setFirstTimeUserOnboarding(false)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.HOME_EXP, RollenceKey.HOME_VARIANT_REVAMP)
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun resetAll() {
        val sharedPrefs = context.getSharedPreferences(KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
    }

    @Test
    fun screenShotVisibleViewNonLoggedIn() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            activityRule.activity.recreate()
        }
        Thread.sleep(10000)
        turnOffAnimation(activityRule.activity)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(
                    activityRule.activity.window.decorView,
                    fileName(),
                    "home".name(false, darkMode = true)
            )
        }
        activityRule.activity.finishAndRemoveTask()
    }

    private fun setupAbTestRemoteConfig() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.NAVIGATION_EXP_TOP_NAV,
                RollenceKey.NAVIGATION_VARIANT_REVAMP)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                CHOOSE_ADDRESS_ROLLENCE_KEY,
                CHOOSE_ADDRESS_ROLLENCE_KEY)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.BALANCE_EXP,
                RollenceKey.BALANCE_VARIANT_NEW)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.HOME_EXP,
                RollenceKey.HOME_VARIANT_REVAMP)
    }

    private fun fileName(suffix: String? = null): String {
        val prefix = TAG
        suffix?.let {
            return "$prefix-$suffix"
        }
        return prefix
    }

    fun String.name(loggedIn: Boolean, darkMode: Boolean = false) = this + (if (loggedIn) "-login" else "-nonlogin") + (if (darkMode) "-dark" else "-light")
}