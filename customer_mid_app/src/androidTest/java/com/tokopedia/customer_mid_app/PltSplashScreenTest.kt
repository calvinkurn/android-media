package com.tokopedia.customer_mid_app

import android.app.Activity
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.tokopedia.analytics.performance.PerformanceAnalyticsUtil
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.home.R
import com.tokopedia.navigation.presentation.activity.MainParentActivity
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.tkpd.ConsumerSplashScreen
import org.junit.Test
import org.junit.Before
import org.junit.Rule
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PltSplashScreenTest {
    val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "test_case_page_load_time"
    private var pltIdlingResource: IdlingResource? = PerformanceAnalyticsUtil.performanceIdlingResource

    @get:Rule
    var activityRule = object: ActivityTestRule<ConsumerSplashScreen>(ConsumerSplashScreen::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupIdlingResource()
            Thread.sleep(2000)
        }
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private fun setupIdlingResource() {
        IdlingRegistry.getInstance().register(pltIdlingResource)
    }

    @Test
    fun testPageLoadTimePerformance() {
        Espresso.onView(ViewMatchers.withId(activityRule.activity.window.decorView.id)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE, "")
        activityRule.activity.finishAndRemoveTask()
    }

    private fun savePLTPerformanceResultData(tag: String, datasource: String) {
        val performanceData = activityRule.activity.pageLoadTimePerformanceCallback?.getPltPerformanceData()
        performanceData?.let {
            activityRule.activity.let {activity ->
                PerformanceDataFileUtils.writePLTPerformanceFile(
                        activity,
                        tag,
                        performanceData,
                        datasource,
                        GqlNetworkAnalyzerInterceptor.getNetworkData()
                )
            }
        }
    }
}
