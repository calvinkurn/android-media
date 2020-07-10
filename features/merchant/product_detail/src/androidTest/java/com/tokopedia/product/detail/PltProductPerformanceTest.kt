package com.tokopedia.product.detail

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.test.application.TestRepeatRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File


/**
 * Created by Yehezkiel on 22/04/20
 */
class PltProductPerformanceTest {
    private val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "pdp_test_case_page_load_time"

    @get:Rule
    var activityRule: ActivityTestRule<ProductDetailActivity> = ActivityTestRule(ProductDetailActivity::class.java, false, false)

    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    @Before
    fun doBeforeRun() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = ProductDetailActivity.createIntent(context, "220891000")
        activityRule.launchActivity(intent)
        activityRule.activity.deleteDatabase("tokopedia_graphql")
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
                    performanceData)
        }
    }
}