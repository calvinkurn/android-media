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
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

/**
 * Created by DevAra
 * This test will measure home with cache
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class PltHomeCacheDynamicChannelPerformanceTest {
    val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "test_case_page_load_time_cache"
    val TYPE_CACHE = "cache"
    val TYPE_NETWORK = "network"
    val TYPE_FAILED = "failed"

    private var pltIdlingResource: IdlingResource? = PerformanceAnalyticsUtil.performanceIdlingResource

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeTestActivity>(InstrumentationHomeTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            disableCoachMark()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            setupRemoteConfig()
        }
    }

    private fun setupRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfigImpl(
                InstrumentationRegistry.getInstrumentation().context
        )
        remoteConfig.setString(RemoteConfigKey.ENABLE_ASYNC_HOME_SNDSCR, "true")
        remoteConfig.setString(RemoteConfigKey.HOME_ENABLE_PAGINATION, "true")
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Test
    fun testPageLoadTimePerformance() {
        Thread.sleep(8000)
        val datasource = checkDataSource()
        if (isCacheDataSource(datasource)) {
            savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE, datasource)
        }

        activityRule.activity.finishAndRemoveTask()
        Thread.sleep(1000)
    }

    private fun disableCoachMark(){
        val sharedPrefs = InstrumentationRegistry
                .getInstrumentation().context
                .getSharedPreferences(NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false).apply()
    }

    private fun isCacheDataSource(datasource: String): Boolean {
        return datasource == TYPE_CACHE
    }

    private fun savePLTPerformanceResultData(tag: String, datasource: String) {
        val performanceData = activityRule.activity.getPltPerformanceResultData()
        performanceData?.let {
            writePLTPerformanceFile(
                    activityRule.activity,
                    tag,
                    performanceData,
                    datasource,
                    GqlNetworkAnalyzerInterceptor.getNetworkData()
            )
        }
    }

    private fun checkDataSource(): String {
        var datasource = ""

        val performanceData = activityRule.activity.getPltPerformanceResultData()
        performanceData?.let {
            if (activityRule.activity.isFromCache) {
                datasource = TYPE_CACHE
            } else if (!performanceData.isSuccess) {
                datasource = TYPE_FAILED
            } else datasource = TYPE_NETWORK
        }
        return datasource
    }
}
