package com.tokopedia.oneclickcheckout.tracking.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.view.View
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.action.scrollTo
import com.tokopedia.oneclickcheckout.common.action.swipeUpTop
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.interceptor.VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageActivity
import com.tokopedia.oneclickcheckout.preference.list.view.PreferenceListViewHolder
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityTrackingTest {

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/one_click_checkout_order_summary.json"
    }

    @get:Rule
    var activityRule = IntentsTestRule(OrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val gtmLogDBSource = GtmLogDBSource(context)

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val preferenceInterceptor = OneClickCheckoutInterceptor.preferenceInterceptor
    private val logisticInterceptor = OneClickCheckoutInterceptor.logisticInterceptor
    private val promoInterceptor = OneClickCheckoutInterceptor.promoInterceptor
    private val checkoutInterceptor = OneClickCheckoutInterceptor.checkoutInterceptor

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        OneClickCheckoutInterceptor.resetAllCustomResponse()

        OneClickCheckoutInterceptor.setupGraphqlMockResponse(context)
        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        gtmLogDBSource.deleteAll().subscribe()
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun performOrderSummaryPageTrackingActions() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.tv_choose_preference)).perform(scrollTo())
        onView(withId(R.id.tv_choose_preference)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        onView(withId(R.id.nested_bottom_sheet_preference_list)).perform(object : ViewAction {
            override fun getDescription(): String = "scroll to bottom"

            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun perform(uiController: UiController?, view: View) {
                val nestedScrollView = view as NestedScrollView
                nestedScrollView.scrollTo(0, nestedScrollView.getChildAt(0).height)
            }
        })
        onView(withId(R.id.btn_add_preference)).perform(click())

        onView(withId(R.id.tv_choose_preference)).perform(scrollTo())
        onView(withId(R.id.tv_choose_preference)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(1, object : ViewAction {
            override fun getDescription(): String = "perform click gear"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<ImageView>(R.id.iv_edit_preference).callOnClick()
            }
        }))

        onView(withId(R.id.tv_choose_preference)).perform(scrollTo())
        onView(withId(R.id.tv_choose_preference)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(swipeUpTop())
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(1, object : ViewAction {
            override fun getDescription(): String = "perform click gunakan"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<Typography>(R.id.tv_choose_preference).callOnClick()
            }
        }))

        onView(withId(R.id.tv_shipping_price)).perform(scrollTo())
        onView(withId(R.id.tv_shipping_price)).perform(click())
        onView(withText("AnterAja")).perform(click())

        onView(withId(R.id.ticker_shipping_promo)).perform(scrollTo())
        promoInterceptor.customValidateUseResponseString = VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
        onView(withId(R.id.ticker_action)).perform(click())

        onView(withId(R.id.btn_pay)).perform(scrollTo())
        onView(withId(R.id.btn_pay)).perform(click())

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }
}