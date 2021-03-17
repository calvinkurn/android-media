package com.tokopedia.product.detail.screenshot

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.instrumentation.test.R
import com.tokopedia.product.detail.ProductDetailActivityCommonTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions.getAllViewsViewHolder
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.ViewUtils
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yehezkiel on 18/01/21
 */
abstract class BaseProductDetailScreenShotTest {

    @get:Rule
    var activityCommonRule: ActivityTestRule<ProductDetailActivityCommonTest> = ActivityTestRule(ProductDetailActivityCommonTest::class.java, false, false)

    @Before
    fun doBeforeRun() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        //Always use mock response
        setupGraphqlMockResponse(createMockModelConfig())
        setupDarkModeTest(forceDarkMode())
        val intent = ProductDetailActivityCommonTest.createIntent(context, "1060957410")
        activityCommonRule.launchActivity(intent)
    }

    @Test
    fun screenShot() {
        waitForData()
        scrollToBottom() // trigger recom listener
        scrollToTop()
        waitForData()

        val views: MutableList<View?> = mutableListOf()
        getAllViewsViewHolder(com.tokopedia.product.detail.R.id.rv_pdp, activityCommonRule.activity.getAdapterTotalSize() - 2) { view, index ->
            Thread.sleep(2000)
            views.add(view)
        }
        ViewUtils.mergeScreenShot(filePrefix(), "test", views)

        activityCommonRule.activity.finishAndRemoveTask()
    }

    abstract fun forceDarkMode(): Boolean

    abstract fun filePrefix(): String

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollToTop() {
        Espresso.onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(
                        RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1)
                )
    }

    private fun scrollToBottom() {
        Espresso.onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(
                        RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(activityCommonRule.activity.getLastPositionIndex())
                )
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("pdpGetLayout", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_get_layout), FIND_BY_CONTAINS)
                addMockResponse("GetPdpGetData", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_p2_get_data), FIND_BY_CONTAINS)
                addMockResponse("discussionMostHelpful", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_p2_other), FIND_BY_CONTAINS)
                addMockResponse("productRecommendation", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_get_recom), FIND_BY_CONTAINS)
                return this
            }
        }
    }
}