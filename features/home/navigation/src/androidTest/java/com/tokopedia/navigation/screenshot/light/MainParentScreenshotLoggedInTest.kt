package com.tokopedia.navigation.screenshot.light

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
class MainParentScreenshotLoggedInTest {
    val TAG = "MainParentScreenshotTest"
    val KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION"
    val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING"

    @get:Rule
    var activityRule = object: ActivityTestRule<MainParentActivity>(MainParentActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(MainHomeMockResponseConfig())
            setupDarkModeTest(false)
            setupHomeEnvironment()
            setupAbTestRemoteConfig()
            disableCoachMark(context)
            InstrumentationAuthHelper.clearUserSession()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private fun setupHomeEnvironment() {
        val userSession = UserSession(context)
        userSession.setFirstTimeUserOnboarding(false)
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun resetAll() {
        val sharedPrefs = context.getSharedPreferences(KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
    }

    @Test
    fun screenShotVisibleViewLoggedIn() {
        Thread.sleep(10000)
        turnOffAnimation(activityRule.activity)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(
                    activityRule.activity.window.decorView,
                    fileName(),
                    "home".name(true)
            )
        }
        activityRule.activity.finishAndRemoveTask()
    }

    private fun setupAbTestRemoteConfig() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.BALANCE_EXP,
                RollenceKey.BALANCE_VARIANT_NEW)
    }

    private fun fileName(suffix: String? = null): String {
        val prefix = TAG
        suffix?.let {
            return "$prefix-$suffix"
        }
        return prefix
    }

    private fun String.name(loggedIn: Boolean) = this + (if (loggedIn) "-login" else "-nonlogin")
}