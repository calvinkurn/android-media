package com.tokopedia.product.detail.analytics

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ProductDetailLoadTimeMonitoringListener
import com.tokopedia.product.detail.presentation.InstrumentTestAddToCartBottomSheet
import com.tokopedia.product.detail.util.ProductDetailIdlingResource
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.viewholder.ProductDiscussionMostHelpfulViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductDiscussionQuestionViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.variant_common.view.holder.VariantChipViewHolder
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder
import com.tokopedia.variant_common.view.holder.VariantImageViewHolder
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProductDetailActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<ProductDetailActivity> = object : IntentsTestRule<ProductDetailActivity>(ProductDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            clearLogin()
        }

        override fun getActivityIntent(): Intent {
            return ProductDetailActivity.createIntent(targetContext, PRODUCT_ID)
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
            productDetailLoadTimeMonitoringListener.onStartPltListener()
            activity.productDetailLoadTimeMonitoringListener = productDetailLoadTimeMonitoringListener
            markAsIdleIfPltIsSucceed()
        }
    }

    val productDetailLoadTimeMonitoringListener = object : ProductDetailLoadTimeMonitoringListener {
        override fun onStartPltListener() {
            ProductDetailIdlingResource.increment()
        }

        override fun onStopPltListener() {
            ProductDetailIdlingResource.decrement()
        }
    }

    @Before
    fun setup() {
        setUpTimeoutIdlingResource()
        IdlingRegistry.getInstance().register(ProductDetailIdlingResource.idlingResource)
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(ProductDetailMockResponse())
        intendingIntent()
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(ProductDetailIdlingResource.idlingResource)
    }

    @Test
    fun validateClickBuyIsLogin() {
        actionTest {
            fakeLogin()
            clickVariantTest()
            clickBuyNow()
        } assertTest {
            waitForTrackerSent()
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, BUTTON_BUY_LOGIN_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickAddToCartIsLogin() {
        actionTest {
            fakeLogin()
            clickVariantTest()
            clickAddToCart()
        } assertTest {
            if (addToCartBottomSheetIsVisible() == true) {
                performClose(activityRule)
                waitForTrackerSent()
                validate(gtmLogDBSource, targetContext, ADD_TO_CART_LOGIN_PATH)
                finishTest()
            } else {
                performClose(activityRule)
            }
        }
    }

    @Test
    fun validateClickBuyIsNonLogin() {
        actionTest {
            clickVariantTest()
            clickBuyNow()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, BUTTON_BUY_NON_LOGIN_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickAddToCartIsNonLogin() {
        actionTest {
            clickVariantTest()
            clickAddToCart()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, ADD_TO_CART_NON_LOGIN_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickGuideOnSizeChart() {
        actionTest {
            clickSeeGuideSizeChart()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, GUIDE_ON_SIZE_CHART_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickTabDiscussion() {
        actionTest {
            fakeLogin()
            clickTabDiscussion()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, DISCUSSION_PRODUCT_TAB_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickSeeAllDiscussion() {
        actionTest {
            fakeLogin()
            clickSeeAllDiscussion()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, SEE_ALL_ON_LATEST_DISCUSSION_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickThreadDetail() {
        actionTest {
            fakeLogin()
            clickThreadDetailDiscussion()
        } assertTest {
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, THREAD_DETAIL_ON_DISCUSSION_PATH)
            finishTest()
        }
    }

    private fun clickSeeAllDiscussion() {
        onView(withId(R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(allOf(withId(R.id.productDiscussionMostHelpfulSeeAll))), scrollTo()))
        val viewInteraction = onView(withId(R.id.rv_pdp)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<ProductDiscussionMostHelpfulViewHolder>(13, clickChildViewWithId(R.id.productDiscussionMostHelpfulSeeAll)))
    }

    private fun clickThreadDetailDiscussion() {
        onView(withId(R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(allOf(withId(R.id.productDiscussionMostHelpfulQuestions))), scrollTo()))
        val viewInteraction = onView(withId(R.id.productDiscussionMostHelpfulQuestions)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<ProductDiscussionQuestionViewHolder>(0, clickChildViewWithId(R.id.productDetailDiscussionThread)))
    }

    private fun clickTabDiscussion() {
        onView(withId(R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(allOf(withId(R.id.mini_social_proof_recycler_view))), scrollTo()))
        onView(allOf(withId(R.id.chipSocialProofItem)))
                .check(matches(isDisplayed()))
                .perform(click())
    }

    private fun setUpTimeoutIdlingResource() {
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
    }

    private fun waitForData() {
        Thread.sleep(5000L)
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun addToCartBottomSheetIsVisible(): Boolean? {
        val addToCartBottomSheet = activityRule.activity.supportFragmentManager.findFragmentByTag("ADD_TO_CART") as? InstrumentTestAddToCartBottomSheet
        return addToCartBottomSheet?.isVisible
    }

    private fun clickVariantTest() {
        onView(withId(R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(allOf(withId(R.id.rvContainerVariant))), scrollTo()))
        val viewInteraction = onView(allOf(withId(R.id.rvContainerVariant))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<VariantImageViewHolder>(0, clickChildViewWithId(R.id.variantImgContainer)))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<VariantChipViewHolder>(1, clickChildViewWithId(R.id.containerChipVariant)))
    }

    private fun clickSeeGuideSizeChart() {
        onView(withId(R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(allOf(withId(R.id.rvContainerVariant))), scrollTo()))
        val viewInteraction = onView(allOf(withId(R.id.rvContainerVariant))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<VariantContainerViewHolder>(1, clickChildViewWithId(R.id.txtVariantGuideline)))
    }

    private fun waitForTrackerSent() {
        Thread.sleep(4000L)
    }

    private fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    private fun clearLogin() {
        InstrumentationAuthHelper.clearUserSession()
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun markAsIdleIfPltIsSucceed() {
        val performanceData = activityRule.activity.pageLoadTimePerformanceMonitoring?.getPltPerformanceData()
        if (performanceData?.isSuccess == true) {
            productDetailLoadTimeMonitoringListener.onStopPltListener()
        }
    }

    companion object {
        const val PRODUCT_ID = "1267836204"
        const val ADD_TO_CART_LOGIN_PATH = "tracker/merchant/product_detail/pdp_add_to_cart_choose_variant_login.json"
        const val ADD_TO_CART_NON_LOGIN_PATH = "tracker/merchant/product_detail/pdp_add_to_cart_choose_variant_non_login.json"
        const val BUTTON_BUY_LOGIN_PATH = "tracker/merchant/product_detail/pdp_button_buy_now_choose_variant_login.json"
        const val BUTTON_BUY_NON_LOGIN_PATH = "tracker/merchant/product_detail/pdp_button_buy_now_choose_variant_non_login.json"
        const val GUIDE_ON_SIZE_CHART_PATH = "tracker/merchant/product_detail/pdp_guide_on_size_chart.json"
        const val SEE_ALL_ON_LATEST_DISCUSSION_PATH = "tracker/merchant/product_detail/pdp_click_see_all_on_latest_discussion.json"
        const val THREAD_DETAIL_ON_DISCUSSION_PATH = "tracker/merchant/product_detail/pdp_click_thread_detail_on_discussion.json"
        const val DISCUSSION_PRODUCT_TAB_PATH = "tracker/merchant/product_detail/pdp_click_discussion_product_tab.json"
    }
}