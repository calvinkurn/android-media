package com.tokopedia.product.detail.screenshot

import android.content.Context
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
import com.tokopedia.test.application.espresso_component.CommonActions.findViewAndScreenShot
import com.tokopedia.test.application.espresso_component.CommonActions.findViewHolderAndScreenshot
import com.tokopedia.test.application.espresso_component.CommonActions.screenShotFullRecyclerView
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yehezkiel on 18/01/21
 */
abstract class BaseProductDetailScreenShotTest {

    companion object {
        const val KEY_CONTENT = "product_content"
        const val KEY_SOCIAL_PROOF = "social_proof_mini"
        const val KEY_REVIEW = "review"
        const val KEY_VARIANT = "variant_options"
        const val KEY_INFO = "protection"
        const val KEY_SHOP = "shop_credibility"
        const val KEY_DISCUSSION = "discussion_faq"
        const val KEY_RECOM = "pdp_5"
    }

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
        val activity = activityCommonRule.activity
        waitForData()
        scrollToBottom() // trigger recom listener
        scrollToTop()

        //Screenshot visible screen
        takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "top")

        //Screenshot full recycler view
        screenShotFullRecyclerView(com.tokopedia.product.detail.R.id.rv_pdp,
                1, // exclude media at position 0
                activityCommonRule.activity.getAdapterTotalSize() - 1,  //Here I dont want include last position in my recyclerview
                "${filePrefix()}-full")

        //Screenshot partial view
        findViewAndScreenShot(com.tokopedia.product.detail.R.id.base_btn_action, filePrefix(), "button")

        //Screenshot per-viewholder
        findViewHolderAndScreenshot(com.tokopedia.product.detail.R.id.rv_pdp, activity.getPositionViewHolderByName(KEY_CONTENT), filePrefix(), "content")
        findViewHolderAndScreenshot(com.tokopedia.product.detail.R.id.rv_pdp, activity.getPositionViewHolderByName(KEY_SOCIAL_PROOF), filePrefix(), "social proof")
        findViewHolderAndScreenshot(com.tokopedia.product.detail.R.id.rv_pdp, activity.getPositionViewHolderByName(KEY_REVIEW), filePrefix(), "review")
        findViewHolderAndScreenshot(com.tokopedia.product.detail.R.id.rv_pdp, activity.getPositionViewHolderByName(KEY_VARIANT), filePrefix(), "variant")
        findViewHolderAndScreenshot(com.tokopedia.product.detail.R.id.rv_pdp, activity.getPositionViewHolderByName(KEY_INFO), filePrefix(), "info")
        findViewHolderAndScreenshot(com.tokopedia.product.detail.R.id.rv_pdp, activity.getPositionViewHolderByName(KEY_SHOP), filePrefix(), "shop info")
        findViewHolderAndScreenshot(com.tokopedia.product.detail.R.id.rv_pdp, activity.getPositionViewHolderByName(KEY_DISCUSSION), filePrefix(), "discussion", true)
        findViewHolderAndScreenshot(com.tokopedia.product.detail.R.id.rv_pdp, activity.getPositionViewHolderByName(KEY_RECOM), filePrefix(), "recom", true)

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
        val activity = activityCommonRule.activity
        Espresso.onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(
                        RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(activity.getPositionViewHolderByName("pdp_5"))
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