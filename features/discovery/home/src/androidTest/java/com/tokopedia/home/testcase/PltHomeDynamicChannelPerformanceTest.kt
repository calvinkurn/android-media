package com.tokopedia.home.testcase

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.PerformanceAnalyticsUtil
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils.writePLTPerformanceFile
import com.tokopedia.home.R
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import com.tokopedia.test.application.util.setupTotalSizeInterceptor
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class PltHomeDynamicChannelPerformanceTest {
    val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "test_case_page_load_time"
    private var pltIdlingResource: IdlingResource? = PerformanceAnalyticsUtil.performanceIdlingResource

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            disableCoachMark()
            setupGraphqlMockResponseWithCheck(HomeMockResponseConfig())
            setupTotalSizeInterceptor(listOf("dynamicPosition", "getDynamicChannel"))
            setupRemoteConfig()
            setupIdlingResource()
            Thread.sleep(2000)
        }
    }

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

    private fun disableCoachMark(){
        val sharedPrefs = InstrumentationRegistry
                .getInstrumentation().context
                .getSharedPreferences(NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Test
    fun testPageLoadTimePerformance() {
        onView(ViewMatchers.withId(R.id.home_fragment_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
        activityRule.activity.deleteDatabase("HomeCache.db")
        activityRule.activity.finishAndRemoveTask()
        Thread.sleep(2000)
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getPltPerformanceResultData()
        performanceData?.let {
            var datasource = ""
            if (activityRule.activity.isFromCache) {
                datasource = "cache"
            } else if (!performanceData.isSuccess) {
                datasource = "failed"
            } else datasource = "network"
            writePLTPerformanceFile(
                    activityRule.activity,
                    tag,
                    performanceData,
                    datasource,
                    GqlNetworkAnalyzerInterceptor.getNetworkData()
            )
        }
    }
}
