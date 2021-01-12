package com.tokopedia.product.detail

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.instrumentation.test.R
import com.tokopedia.product.detail.util.ProductDetailActivityTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.isDarkMode
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yehezkiel on 10/01/21
 */
class ProductDetailScreenshotTest {

    var darkModePrefixKey = "product_detail"

    @get:Rule
    var activityRule: ActivityTestRule<ProductDetailActivityTest> = ActivityTestRule(ProductDetailActivityTest::class.java, false, false)

    @Before
    fun doBeforeRun() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        setupGraphqlMockResponseWithCheck(createMockModelConfig())
        setupDarkModeTest()
        darkModePrefixKey += if (isDarkMode()) "-dark" else "-light"
        val intent = ProductDetailActivityTest.createIntent(context, "1060957410")
        activityRule.launchActivity(intent)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("pdpGetLayout", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_get_layout), FIND_BY_CONTAINS)
                addMockResponse("GetPdpGetData", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_p2_get_data), FIND_BY_CONTAINS)
//                addMockResponse("ImageReview", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_p2_other), FIND_BY_CONTAINS)
                addMockResponse("productRecommendation", InstrumentationMockHelper.getRawString(context, R.raw.response_mock_data_pdp_get_recom), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    @Test
    fun screenShot() {
        waitForData()
        scrollToTop()
        activityRule.activity.takeScreenShot(darkModePrefixKey + "-1")

        Thread.sleep(3000)
        scrollToCenter()
        activityRule.activity.takeScreenShot(darkModePrefixKey + "-2")

        Thread.sleep(3000)
        scrollToCenter2()
        activityRule.activity.takeScreenShot(darkModePrefixKey + "-3")


        Thread.sleep(3000)
        scrollToBottom()
        waitForData()
        scrollToBottom()
        activityRule.activity.takeScreenShot(darkModePrefixKey + "-4")

        activityRule.activity.finishAndRemoveTask()
    }

    private fun scrollToTop() {
        onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .check(matches(isDisplayed()))
                .perform(
                        RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(ViewMatchers.withId(com.tokopedia.product.detail.R.id.product_detail_info_title))), ViewActions.scrollTo())
                )
    }

    private fun scrollToCenter() {
        onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .check(matches(isDisplayed()))
                .perform(
                        RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(8)
                )
    }

    private fun scrollToCenter2() {
        onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .check(matches(isDisplayed()))
                .perform(
                        RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(ViewMatchers.withId(com.tokopedia.product.detail.R.id.productDiscussionMostHelpfulSeeAll))), ViewActions.scrollTo())
                )
    }

    private fun scrollToBottom() {
        onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .check(matches(isDisplayed()))
                .perform(
                        RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(activityRule.activity.getLastPositionIndex())
                )
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

}