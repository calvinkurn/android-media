package com.tokopedia.product.detail

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.instrumentation.test.R
import com.tokopedia.product.detail.util.ProductDetailActivityTest
import com.tokopedia.test.application.TestRepeatRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yehezkiel on 10/01/21
 */
class ProductDarkModeTest {

    @get:Rule
    var activityRule: ActivityTestRule<ProductDetailActivityTest> = ActivityTestRule(ProductDetailActivityTest::class.java, false, false)

    @Before
    fun doBeforeRun() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        setupGraphqlMockResponseWithCheck(createMockModelConfig())

        val intent = ProductDetailActivityTest.createIntent(context, "1060957410")
        activityRule.launchActivity(intent)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("pdpGetLayout", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_get_layout), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    @Test
    fun testPageLoadTimePerformance() {
        waitForData()
        activityRule.activity.takeScreenShot()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

}