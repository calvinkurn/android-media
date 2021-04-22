package com.tokopedia.navigation.screenshot

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.navigation.com.tokopedia.navigation.mock.MainHomeMockResponseConfig
import com.tokopedia.navigation.presentation.activity.MainParentActivity
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TAG = "MainParentScreenshotTesting"
/**
 * Created by devarafikry on 12/04/21.
 */
class MainParentScreenshotTest {
    val KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION"
    val KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING"

    @get:Rule
    var activityRule = object: ActivityTestRule<MainParentActivity>(MainParentActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(MainHomeMockResponseConfig())
            setupDarkModeTest(false)
            setupHomeEnvironment()
        }
    }

    private fun setupHomeEnvironment() {
        val userSession = UserSession(context)
        userSession.setFirstTimeUserOnboarding(false)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                AbTestPlatform.HOME_EXP, AbTestPlatform.HOME_VARIANT_REVAMP)
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun resetAll() {
        val sharedPrefs = context.getSharedPreferences(KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
    }

    @Test
    fun screenShotVisibleView() {
        Thread.sleep(10000)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(
                    activityRule.activity.window.decorView,
                    fileName(),
                    "home"
            )
        }
        activityRule.activity.finishAndRemoveTask()
    }

    private fun fileName(suffix: String? = null): String {
        val prefix = TAG
        suffix?.let {
            return "$prefix-$suffix"
        }
        return prefix
    }
}