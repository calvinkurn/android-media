package com.tokopedia.home.testcase

import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils.writePLTPerformanceFile
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.test.application.TestRepeatRule
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
    var activityRule: ActivityTestRule<InstrumentationHomeTestActivity> = ActivityTestRule(InstrumentationHomeTestActivity::class.java)

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
                    datasource)
        }
    }
}
