package com.tokopedia.oneclickcheckout.tracking.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.interceptor.VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageActivity
import org.junit.After
import org.junit.Assert.assertEquals
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
    fun happyFlow_ChangeCourier() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.ticker_shipping_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_shipping_name)).check(matches(withText("Pengiriman Reguler")))
        onView(withId(R.id.tv_shipping_duration)).check(matches(withText("Durasi 2-4 hari - Kurir Rekomendasi")))
        onView(withId(R.id.tv_shipping_price)).check(matches(withText("Rp15.000")))

        onView(withId(R.id.tv_shipping_price)).perform(click())
        onView(withText("AnterAja")).perform(click())

        onView(withId(R.id.ticker_shipping_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_shipping_name)).check(matches(withText("Pengiriman Reguler")))
        onView(withId(R.id.tv_shipping_duration)).check(matches(withText("Durasi 2-4 hari - AnterAja")))
        onView(withId(R.id.tv_shipping_price)).check(matches(withText("Rp16.000")))
        onView(withId(R.id.nested_scroll_view)).perform(swipeUp())
        onView(withId(R.id.tv_total_payment_value)).check(matches(withText("Rp117.000")))
        onView(withId(R.id.btn_pay)).check(matches(withText("Bayar")))

        onView(withId(R.id.ticker_shipping_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.ticker_shipping_promo_description)).check(matches(withText("Tersedia Bebas Ongkir (4-6 hari)")))

        promoInterceptor.customValidateUseResponseString = VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE

        onView(withId(R.id.ticker_action)).perform(click())

        onView(withId(R.id.ticker_shipping_promo)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.GONE, view.visibility)
        }
        onView(withId(R.id.tv_shipping_name)).check(matches(withText(R.string.lbl_osp_free_shipping)))
        onView(withId(R.id.tv_shipping_duration)).check(matches(withText("Durasi 4-6 hari")))
        onView(withId(R.id.tv_shipping_price)).check(matches(withText(R.string.lbl_osp_free_shipping_only_price)))

        onView(withId(R.id.nested_scroll_view)).perform(swipeUp())
        onView(withId(R.id.tv_total_payment_value)).check(matches(withText("Rp101.000")))
        onView(withId(R.id.btn_pay)).check(matches(withText("Bayar")))
        onView(withId(R.id.btn_pay)).perform(click())

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }
}