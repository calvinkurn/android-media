package com.tokopedia.checkout.journey.simple

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.RevampShipmentActivity
import com.tokopedia.checkout.interceptor.CheckoutInterceptor
import com.tokopedia.checkout.robot.checkoutPageRevamp
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.checkout.test.R as checkouttestR

@UiTest
class CheckoutRevampTokoNowSellyTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<RevampShipmentActivity>(RevampShipmentActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var interceptor = CheckoutInterceptor()

    @Before
    fun before() {
        interceptor.setupGraphqlMockResponse(context)
    }

//    private fun setup(safResponse: Int = R.raw.saf_tokonow_selly_response, ratesResponse: Int = R.raw.ratesv3_selly_default_response) {
//        setupGraphqlMockResponse {
//            addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, safResponse), MockModelConfig.FIND_BY_CONTAINS)
//            addMockResponse(SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.save_shipment_default_response), MockModelConfig.FIND_BY_CONTAINS)
//            addMockResponse(RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, ratesResponse), MockModelConfig.FIND_BY_CONTAINS)
//            addMockResponse(SELLY_KEY, InstrumentationMockHelper.getRawString(context, R.raw.selly_default_response), MockModelConfig.FIND_BY_CONTAINS)
//            addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.validate_use_selly_default_response), MockModelConfig.FIND_BY_CONTAINS)
//            addMockResponse(PAYMENT_FEE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.payment_fee_default_response), MockModelConfig.FIND_BY_CONTAINS)
//            addMockResponse(CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.checkout_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
//        }
//    }

    @Test
    fun selly_default_flow() {
//        setup()
        interceptor.cartInterceptor.customSafResponsePath = checkouttestR.raw.saf_tokonow_selly_response
        interceptor.logisticInterceptor.customRatesResponsePath = checkouttestR.raw.ratesv3_selly_default_response
        interceptor.promoInterceptor.customValidateUseResponsePath = checkouttestR.raw.validate_use_selly_default_response
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
            assertHasSellySelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = "Rp0",
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp135.000",
                itemOriginalPrice = null,
                shippingTotalPrice = "Rp0",
                shippingOriginalPrice = "Rp15.000",
                totalPrice = "Rp138.900"
            )
            assertPlatformFee(
                activityRule,
                fee = "Rp3.000",
                originalFee = null
            )
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @Test
    fun selly_change_to_2hr() {
        interceptor.cartInterceptor.customSafResponsePath = checkouttestR.raw.saf_tokonow_selly_response
        interceptor.logisticInterceptor.customRatesResponsePath = checkouttestR.raw.ratesv3_selly_default_response
        interceptor.promoInterceptor.customValidateUseResponsePath = checkouttestR.raw.validate_use_selly_default_response
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
            assertHasSellySelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = "Rp0",
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            waitForData()
            interceptor.promoInterceptor.customValidateUseResponsePath = checkouttestR.raw.validate_use_selly_2hr_response
            click2hrOption(activityRule)
            waitForData()
            assertHas2hrSelected(
                activityRule,
                titleSelly = "Terjadwal",
                originalPriceSelly = "Rp15.000",
                discountedPriceSelly = "Rp0",
                etaSelly = "Tiba hari ini, 14:00 - 15:00",
                title2hr = "2 Jam Tiba",
                originalPrice2hr = "Rp23.000",
                discountedPrice2hr = "Rp0"
            )
            assertShoppingSummary(
                activityRule,
                itemTotalPrice = "Rp135.000",
                itemOriginalPrice = null,
                shippingTotalPrice = "Rp0",
                shippingOriginalPrice = "Rp23.000",
                totalPrice = "Rp139.000"
            )
            assertPlatformFee(
                activityRule,
                fee = "Rp3.000",
                originalFee = null
            )
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

//    @Test
//    fun tokoNowWithFailedDefaultDuration_PassedAnalyticsAndPaymentIntent() {
//        setup(safResponse = R.raw.saf_bundle_tokonow_with_failed_default_duration_response)
//        activityRule.launchActivity(null)
//
//        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))
//
//        checkoutPageRevamp {
//            // Wait for SAF
//            waitForData()
//            // Wait for Rates
//            waitForData()
//            // Wait for Validate Use
//            waitForData()
//            assertHasSingleShipmentSelected(
//                activityRule,
//                title = "Bebas Ongkir (Rp0)",
//                eta = "Estimasi tiba hari ini"
//            )
//            clickChoosePaymentButton(activityRule)
//        } validateAnalytics {
//            waitForData()
//            assertGoToPayment()
//        }
//    }
//
//    @Test
//    fun tokoNowWithAdditionalPrice_PassedAnalyticsAndPaymentIntent() {
//        setup(ratesResponse = R.raw.ratesv3_tokonow_with_additional_price_response)
//        activityRule.launchActivity(null)
//
//        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))
//
//        checkoutPageRevamp {
//            // Wait for SAF
//            waitForData()
//            // Wait for Rates
//            waitForData()
//            // Wait for Validate Use
//            waitForData()
//            assertHasSingleShipmentSelected(
//                activityRule,
//                title = "Bebas Ongkir (Rp13.000 Rp8.000)",
//                originalPrice = "(Rp13.000 ",
//                discountedPrice = " Rp8.000)",
//                eta = "Estimasi tiba hari ini",
//                message = "Pengiriman melebihi limit bebas ongkir, kamu cukup bayar Rp8.000"
//            )
//            clickChoosePaymentButton(activityRule)
//        } validateAnalytics {
//            waitForData()
//            assertGoToPayment()
//        }
//    }
//
//    @Test
//    fun tokoNowWithNormalPrice_PassedAnalyticsAndPaymentIntent() {
//        setup(ratesResponse = R.raw.ratesv3_tokonow_with_normal_price_response)
//        activityRule.launchActivity(null)
//
//        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))
//
//        checkoutPageRevamp {
//            // Wait for SAF
//            waitForData()
//            // Wait for Rates
//            waitForData()
//            assertHasSingleShipmentSelected(
//                activityRule,
//                title = "Gojek (Rp13.000)",
//                eta = "Estimasi tiba hari ini",
//                message = "Kuota Bebas Ongkirmu habis"
//            )
//            clickChoosePaymentButton(activityRule)
//        } validateAnalytics {
//            waitForData()
//            assertGoToPayment()
//        }
//    }

    @After
    fun cleanup() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }

    companion object {
        private const val SHIPMENT_ADDRESS_FORM_KEY = "shipmentAddressFormV4"
        private const val SAVE_SHIPMENT_KEY = "save_shipment"
        private const val RATES_V3_KEY = "ratesV3"
        private const val SELLY_KEY = "ongkirGetScheduledDeliveryRates"
        private const val VALIDATE_USE_KEY = "validate_use_promo_revamp"
        private const val PAYMENT_FEE_KEY = "getPaymentFeeCheckout"
        private const val CHECKOUT_KEY = "checkout"
    }
}
