package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.view.View
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
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.interceptor.RATES_RESPONSE_WITH_INSURANCE
import com.tokopedia.oneclickcheckout.common.interceptor.VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class OrderSummaryPageActivityTest {

    @get:Rule
    var activityRule = IntentsTestRule(OrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val preferenceInterceptor = OneClickCheckoutInterceptor.preferenceInterceptor
    private val logisticInterceptor = OneClickCheckoutInterceptor.logisticInterceptor
    private val promoInterceptor = OneClickCheckoutInterceptor.promoInterceptor
    private val checkoutInterceptor = OneClickCheckoutInterceptor.checkoutInterceptor

    private fun resetCustomInterception() {
        cartInterceptor.customGetOccCartThrowable = null
        cartInterceptor.customGetOccCartResponseString = null
        cartInterceptor.customUpdateCartOccThrowable = null
        cartInterceptor.customUpdateCartOccResponseString = null

        preferenceInterceptor.customGetPreferenceListThrowable = null
        preferenceInterceptor.customGetPreferenceListResponseString = null
        preferenceInterceptor.customSetDefaultPreferenceThrowable = null
        preferenceInterceptor.customSetDefaultPreferenceResponseString = null

        logisticInterceptor.customRatesThrowable = null
        logisticInterceptor.customRatesResponseString = null

        promoInterceptor.customValidateUseThrowable = null
        promoInterceptor.customValidateUseResponseString = null

        checkoutInterceptor.customCheckoutThrowable = null
        checkoutInterceptor.customCheckoutResponseString = null
    }

    @Before
    fun setup() {
        OneClickCheckoutInterceptor.resetAllCustomResponse()
        OneClickCheckoutInterceptor.setupGraphqlMockResponse(context)
        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun happyFlow_DirectCheckout() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.tv_shop_name)).check(matches(withText("tokocgk")))
        onView(withId(R.id.tv_shop_location)).check(matches(withText("Kota Yogyakarta")))
        onView(withId(R.id.tv_product_name)).check(matches(withText("Product1")))
        onView(withId(R.id.tv_product_price)).check(matches(withText("Rp100.000")))
        onView(withId(R.id.tv_product_slash_price)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.GONE, view.visibility)
        }
        onView(withId(R.id.iv_free_shipping)).check(matches(isDisplayed()))
        onView(withId(R.id.et_qty)).check(matches(withText("1")))

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
        onView(withId(R.id.btn_pay)).perform(click())

        val intents = Intents.getIntents()
        val paymentPassData = intents.first().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)!!
        assertEquals("https://www.tokopedia.com/payment", paymentPassData.redirectUrl)
        assertEquals("transaction_id=123", paymentPassData.queryString)
        assertEquals("POST", paymentPassData.method)
    }

    @Test
    fun happyFlow_AddQuantity() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.btn_qty_plus)).perform(click())
        Thread.sleep(1500)
        onView(withId(R.id.et_qty)).check(matches(withText("2")))

        onView(withId(R.id.nested_scroll_view)).perform(swipeUp())
        onView(withId(R.id.tv_total_payment_value)).check(matches(withText("Rp216.000")))
        onView(withId(R.id.btn_pay)).check(matches(withText("Bayar")))
        onView(withId(R.id.btn_pay)).perform(click())

        val intents = Intents.getIntents()
        val paymentPassData = intents.first().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)!!
        assertEquals("https://www.tokopedia.com/payment", paymentPassData.redirectUrl)
        assertEquals("transaction_id=123", paymentPassData.queryString)
        assertEquals("POST", paymentPassData.method)
    }

    @Test
    fun happyFlow_CheckInsurance() {
        logisticInterceptor.customRatesResponseString = RATES_RESPONSE_WITH_INSURANCE

        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.nested_scroll_view)).perform(swipeUp())
        onView(withId(R.id.cb_insurance)).perform(click())
        onView(withId(R.id.cb_insurance)).check(matches(isChecked()))
        onView(withId(R.id.tv_total_payment_value)).check(matches(withText("Rp117.000")))
        onView(withId(R.id.btn_pay)).check(matches(withText("Bayar")))
        onView(withId(R.id.btn_pay)).perform(click())

        val intents = Intents.getIntents()
        val paymentPassData = intents.first().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)!!
        assertEquals("https://www.tokopedia.com/payment", paymentPassData.redirectUrl)
        assertEquals("transaction_id=123", paymentPassData.queryString)
        assertEquals("POST", paymentPassData.method)
    }

    @Test
    fun happyFlow_ChooseBboFromTicker() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

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

        val intents = Intents.getIntents()
        val paymentPassData = intents.first().getParcelableExtra<PaymentPassData>(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)!!
        assertEquals("https://www.tokopedia.com/payment", paymentPassData.redirectUrl)
        assertEquals("transaction_id=123", paymentPassData.queryString)
        assertEquals("POST", paymentPassData.method)
    }

    @Test
    fun reloadPage_ErrorGetOccCartPage() {
        activityRule.launchActivity(null)
        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.iv_edit_preference)).check(matches(isDisplayed()))

        cartInterceptor.customGetOccCartThrowable = IOException()

        onView(withId(R.id.iv_edit_preference)).perform(click())

        onView(withId(R.id.global_error)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.VISIBLE, view.visibility)
        }
    }
}