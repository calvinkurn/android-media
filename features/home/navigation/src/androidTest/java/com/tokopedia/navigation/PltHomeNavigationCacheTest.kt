package com.tokopedia.navigation.com.tokopedia.navigation

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

import org.junit.Test

import org.junit.Before
import org.junit.Rule
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PltHomeNavigationCacheTest {
    val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "test_case_page_load_time"
    private var pltIdlingResource: IdlingResource? = PerformanceAnalyticsUtil.performanceIdlingResource

    @get:Rule
    var activityRule = object: ActivityTestRule<MainParentActivity>(MainParentActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupRemoteConfig()
            setupIdlingResource()
            Thread.sleep(2000)
        }
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private fun setupIdlingResource() {
        IdlingRegistry.getInstance().register(pltIdlingResource)
    }

    private fun setupRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfigImpl(
                InstrumentationRegistry.getInstrumentation().context
        )
        remoteConfig.setString(RemoteConfigKey.ENABLE_ASYNC_HOME_SNDSCR, "true")
        remoteConfig.setString(RemoteConfigKey.HOME_ENABLE_PAGINATION, "true")
    }

    @Test
    fun testPageLoadTimePerformance() {
        Espresso.onView(ViewMatchers.withId(R.id.home_fragment_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE, checkDataSource())
        getCurrentActivity()?.finishAndRemoveTask()
    }

    private fun savePLTPerformanceResultData(tag: String, datasource: String) {
        val performanceData = (getCurrentActivity() as? MainParentActivity)?.pageLoadTimePerformanceInterface?.getPltPerformanceData()
        performanceData?.let {
            getCurrentActivity()?.let {activity ->
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

    private fun checkDataSource(): String {
        return (getCurrentActivity() as? MainParentActivity)
                ?.pageLoadTimePerformanceInterface
                ?.getAttribution()?.get("dataSource")?:""
    }

    fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        return currentActivity
    }
}
