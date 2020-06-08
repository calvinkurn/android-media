package com.tokopedia.officialstore.testcase

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.officialstore.environment.InstrumentationOfficialStoreTestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class PltOsHomePerformanceTest {
    private val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "official_store_test_case_page_load_time"
    private var context: Context? = null

    @get:Rule
    var activityRule: ActivityTestRule<InstrumentationOfficialStoreTestActivity> = ActivityTestRule(InstrumentationOfficialStoreTestActivity::class.java)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Before
    fun init(){
        context =  InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
        activityRule.activity.finishAndRemoveTask()
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getPltPerformanceResultData()
        performanceData?.let {
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    tag,
                    it
            )
        }
    }

}
