package com.tokopedia.product.detail

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
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
class ProductDarkModeTest {

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
                return this
            }
        }
    }

    @Test
    fun screenShot() {
        waitForData()
        scrollToTop()
        activityRule.activity.takeScreenShot(darkModePrefixKey + "-top")

        scrollToCenter()
        activityRule.activity.takeScreenShot(darkModePrefixKey + "-center")

        scrollToBottom()
        waitForData()
        scrollToBottom()

        activityRule.activity.takeScreenShot(darkModePrefixKey + "-bottom")
        activityRule.activity.finishAndRemoveTask()
    }

    private fun scrollToTop() {
        onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(7, ViewActions.scrollTo())
                )
    }

    private fun scrollToCenter() {
        onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, ViewActions.scrollTo())
                )
    }

    private fun scrollToBottom() {
        onView(ViewMatchers.withId(com.tokopedia.product.detail.R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(ViewMatchers.withId(com.tokopedia.product.detail.R.id.productDiscussionMostHelpfulSeeAll))), ViewActions.scrollTo()))
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

}