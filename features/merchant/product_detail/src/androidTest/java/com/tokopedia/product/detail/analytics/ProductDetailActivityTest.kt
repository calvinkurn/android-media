package com.tokopedia.product.detail.analytics

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.presentation.InstrumentTestAddToCartBottomSheet
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.variant_common.view.holder.VariantChipViewHolder
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder
import com.tokopedia.variant_common.view.holder.VariantImageViewHolder
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProductDetailActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<ProductDetailActivity> = object: IntentsTestRule<ProductDetailActivity>(ProductDetailActivity::class.java) {
        override fun getActivityIntent(): Intent {
            return ProductDetailActivity.createIntent(targetContext, PRODUCT_ID)
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(ProductDetailMockResponse())
    }

    @Test
    fun validateClickBuyIsLogin() {
        actionTest {
            fakeLogin()
            intendingIntent()
            waitForData()
            clickVariantTest()
            clickBuyNow()
        } assertTest {
            waitForTrackerSent()
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, BUTTON_BUY_LOGIN_PATH)
        }
    }

    @Test
    fun validateClickAddToCartIsLogin() {
        actionTest {
            fakeLogin()
            intendingIntent()
            waitForData()
            clickVariantTest()
            clickAddToCart()
        } assertTest {
            if(addToCartBottomSheetIsVisible() == true) {
                waitForTrackerSent()
                performClose(activityRule)
                validate(gtmLogDBSource, targetContext, ADD_TO_CART_LOGIN_PATH)
            }
        }
    }

    @Test
    fun validateClickBuyIsNonLogin() {
        actionTest {
            clearLogin()
            intendingIntent()
            waitForData()
            clickVariantTest()
            clickBuyNow()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, BUTTON_BUY_NON_LOGIN_PATH)
        }
    }

    @Test
    fun validateClickAddToCartIsNonLogin() {
        actionTest {
            clearLogin()
            intendingIntent()
            waitForData()
            clickVariantTest()
            clickAddToCart()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, ADD_TO_CART_NON_LOGIN_PATH)
        }
    }

    @Test
    fun validateClickGuideOnSizeChart() {
        actionTest {
            intendingIntent()
            waitForData()
            clickSeeGuideSizeChart()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, GUIDE_ON_SIZE_CHART_PATH)
        }
    }

    private fun addToCartBottomSheetIsVisible(): Boolean? {
        val addToCartBottomSheet = activityRule.activity.supportFragmentManager.findFragmentByTag("ADD_TO_CART") as? InstrumentTestAddToCartBottomSheet
        return addToCartBottomSheet?.isVisible
    }

    private fun clickVariantTest() {
        onView(withId(R.id.rv_pdp)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(6, scrollTo())
        )
        Thread.sleep(1000)

        val viewInteraction = onView(allOf(withId(R.id.rvContainerVariant))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<VariantImageViewHolder>(0, clickChildViewWithId(R.id.variantImgContainer)))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<VariantChipViewHolder>(1, clickChildViewWithId(R.id.containerChipVariant)))

        Thread.sleep(1000)
    }

    private fun clickSeeGuideSizeChart() {
        onView(withId(R.id.rv_pdp)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(6, scrollTo())
        )
        Thread.sleep(1000)
        val viewInteraction = onView(allOf(withId(R.id.rvContainerVariant))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<VariantContainerViewHolder>(1, clickChildViewWithId(R.id.txtVariantGuideline)))
    }

    private fun waitForData() {
        Thread.sleep(8000)
    }

    private fun waitForTrackerSent() {
        Thread.sleep(5000)
    }

    private fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    private fun clearLogin() {
        InstrumentationAuthHelper.clearUserSession()
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    companion object {
        const val PRODUCT_ID = "890495024"
        const val ADD_TO_CART_LOGIN_PATH = "tracker/merchant/product_detail/pdp_add_to_cart_choose_variant_login.json"
        const val ADD_TO_CART_NON_LOGIN_PATH = "tracker/merchant/product_detail/pdp_add_to_cart_choose_variant_non_login.json"
        const val BUTTON_BUY_LOGIN_PATH = "tracker/merchant/product_detail/pdp_button_buy_now_choose_variant_login.json"
        const val BUTTON_BUY_NON_LOGIN_PATH = "tracker/merchant/product_detail/pdp_button_buy_now_choose_variant_non_login.json"
        const val GUIDE_ON_SIZE_CHART_PATH = "tracker/merchant/product_detail/pdp_guide_on_size_chart.json"
    }
}