package com.tokopedia.home.screenshot.dark

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.component.name
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.screenshot.HomeScreenshotTestHelper.turnOffAnimation
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.BALANCE_EXP
import com.tokopedia.remoteconfig.RollenceKey.BALANCE_VARIANT_NEW
import com.tokopedia.test.application.annotations.ScreenshotTest

import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 12/04/21.
 */
@ScreenshotTest
class HomeScreenshotNonLoggedInTest {
    private val TAG = "HomeScreenshotTest"
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            gtmLogDBSource.deleteAll().subscribe()
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            setupDarkModeTest(true)
            setupAbTestRemoteConfig()
            disableCoachMark(context)
        }
    }

    @Test
    fun screenShotVisibleViewNonLoggedIn() {
        Thread.sleep(4000)
        turnOffAnimation(activityRule.activity)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(
                    activityRule.activity.window.decorView,
                    fileName(),
                    "dc".name(false, darkMode = true)
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

    private fun setupAbTestRemoteConfig() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                BALANCE_EXP,
                BALANCE_VARIANT_NEW)
    }
}