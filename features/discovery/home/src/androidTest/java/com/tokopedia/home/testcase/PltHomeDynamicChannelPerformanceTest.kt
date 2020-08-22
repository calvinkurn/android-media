package com.tokopedia.home.testcase

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils.writePLTPerformanceFile
import com.tokopedia.config.GlobalConfig
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.InstrumentationTestApp
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

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeTestActivity>(InstrumentationHomeTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponseWithCheck(HomeMockResponseConfig())
            setupTotalSizeInterceptor(listOf("homeData"))
            GlobalConfig.DEBUG = true
            val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as InstrumentationTestApp
            application.enableRemoteConfig(
                    mapOf(
                            Pair("android_enable_home_sndscr", "true"),
                            Pair("android_flag_enable_new_wishlist_page", "true")
                    )
            )
        }
    }

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Before
    fun deleteDatabase() {
        activityRule. activity.deleteDatabase("HomeCache.db")
    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
        activityRule.activity.deleteDatabase("HomeCache.db")
        activityRule.activity.finishAndRemoveTask()
        Thread.sleep(1000)
    }

    private fun waitForData() {
        Thread.sleep(10000)
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
