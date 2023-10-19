package com.tokopedia.checkout.journey.simple

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.RevampShipmentActivity
import com.tokopedia.checkout.interceptor.CheckoutInterceptor
import com.tokopedia.checkout.interceptor.RATES_TOKONOW_WITH_ADDITIONAL_PRICE_RESPONSE_PATH
import com.tokopedia.checkout.interceptor.RATES_TOKONOW_WITH_NORMAL_PRICE_RESPONSE_PATH
import com.tokopedia.checkout.interceptor.SAF_TOKONOW_WITH_FAILED_DEFAULT_DURATION_RESPONSE_PATH
import com.tokopedia.checkout.robot.checkoutPageRevamp
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class CheckoutRevampTokoNowTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<RevampShipmentActivity>(RevampShipmentActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var interceptor = CheckoutInterceptor

    @Before
    fun before() {
        interceptor.resetAllCustomResponse()
        interceptor.setupGraphqlMockResponse(context)
    }

    @Test
    fun tokoNow_PassedAnalyticsAndPaymentIntent() {
        activityRule.launchActivity(null)
        intending(anyIntent())
            .respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            // Wait for Validate Use
            waitForData()
            assertHasSingleShipmentSelected(
                activityRule,
                title = "Bebas Ongkir (Rp0)",
                eta = "Estimasi tiba hari ini"
            )
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithFailedDefaultDuration_PassedAnalyticsAndPaymentIntent() {
        interceptor.cartInterceptor.customSafResponsePath = SAF_TOKONOW_WITH_FAILED_DEFAULT_DURATION_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            // Wait for Validate Use
            waitForData()
            assertHasSingleShipmentSelected(
                activityRule,
                title = "Bebas Ongkir (Rp0)",
                eta = "Estimasi tiba hari ini"
            )
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithAdditionalPrice_PassedAnalyticsAndPaymentIntent() {
        interceptor.logisticInterceptor.customRatesResponsePath = RATES_TOKONOW_WITH_ADDITIONAL_PRICE_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            // Wait for Validate Use
            waitForData()
            assertHasSingleShipmentSelected(
                activityRule,
                title = "Bebas Ongkir (Rp13.000 Rp8.000)",
                originalPrice = "(Rp13.000 ",
                discountedPrice = " Rp8.000)",
                eta = "Estimasi tiba hari ini",
                message = "Pengiriman melebihi limit bebas ongkir, kamu cukup bayar Rp8.000"
            )
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithNormalPrice_PassedAnalyticsAndPaymentIntent() {
        interceptor.logisticInterceptor.customRatesResponsePath = RATES_TOKONOW_WITH_NORMAL_PRICE_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        checkoutPageRevamp {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            assertHasSingleShipmentSelected(
                activityRule,
                title = "Gojek (Rp13.000)",
                eta = "Estimasi tiba hari ini",
                message = "Kuota Bebas Ongkirmu habis"
            )
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @After
    fun cleanup() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }
}
