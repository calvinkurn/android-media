package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityTest {

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
        gtmLogDBSource.deleteAll()
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
    fun test() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.tv_card_header)).check(matches(withText("Pilihan 1")))
        onView(withId(R.id.tv_address_name)).check(matches(withText("Address 1")))
        onView(withId(R.id.tv_address_receiver)).check(matches(withText(" - User 1 (1)")))
        onView(withId(R.id.tv_address_detail)).check(matches(withText("Address Street 1, District 1, City 1, Province 1 1")))
        onView(withId(R.id.lbl_main_preference)).check(matches(isDisplayed()))

        onView(withId(R.id.ticker_shipping_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_shipping_name)).check(matches(withText("Pengiriman Reguler")))
        onView(withId(R.id.tv_shipping_duration)).check(matches(withText("Durasi 2-4 hari - Kurir Rekomendasi")))

        onView(withId(R.id.tv_payment_name)).check(matches(withText("Payment 1")))

        onView(withId(R.id.nested_scroll_view)).perform(swipeUp())
        onView(withId(R.id.tv_total_payment_value)).check(matches(withText("Rp116.000")))
        onView(withId(R.id.btn_pay)).check(matches(withText("Bayar")))
        onView(withId(R.id.btn_pay)).perform(click())

        val intents = Intents.getIntents()
        val paymentPassData = intents.first().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)!!
        assertEquals("https://www.tokopedia.com/payment", paymentPassData.redirectUrl)
        assertEquals("transaction_id=123", paymentPassData.queryString)
        assertEquals("POST", paymentPassData.method)

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }
}