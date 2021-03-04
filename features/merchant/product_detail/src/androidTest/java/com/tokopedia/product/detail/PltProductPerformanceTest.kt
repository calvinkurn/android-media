package com.tokopedia.product.detail

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.instrumentation.test.R
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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
        setupGraphqlMockResponseWithCheck(createMockModelConfig())

        val intent = ProductDetailActivity.createIntent(context, "1060957410")
        activityRule.launchActivity(intent)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("pdpGetLayout", getRawString(context, R.raw.response_mock_data_pdp_get_layout), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        savePLTPerformanceResultData(TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE)
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun waitForData() {
        Thread.sleep(35000)
    }

    private fun savePLTPerformanceResultData(tag: String) {
        val performanceData = activityRule.activity.getPltPerformanceResultData()
        performanceData?.let {
            PerformanceDataFileUtils.writePLTPerformanceFile(
                    activityRule.activity,
                    tag,
                    performanceData,performanceData.attribution[ProductDetailActivity.PRODUCT_PERFORMANCE_MONITORING_VARIANT_KEY] ?: "")
        }
    }
}