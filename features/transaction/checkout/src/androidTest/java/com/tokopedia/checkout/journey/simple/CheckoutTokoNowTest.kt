package com.tokopedia.checkout.journey.simple

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.checkout.robot.checkoutPage
import com.tokopedia.checkout.test.R
import com.tokopedia.checkout.view.ShipmentActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Rule
import org.junit.Test

class CheckoutTokoNowTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<ShipmentActivity>(ShipmentActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDBSource = GtmLogDBSource(context)

    private fun setup(safResponse: Int = R.raw.saf_tokonow_default_response, ratesResponse: Int = R.raw.ratesv3_tokonow_default_response) {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, safResponse), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.save_shipment_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, ratesResponse), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.validate_use_tokonow_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.checkout_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun tokoNow_PassedAnalyticsAndPaymentIntent() {
        setup()
        activityRule.launchActivity(null)

        checkoutPage {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            // Wait for Validate Use
            waitForData()
            assertHasSingleShipmentSelected(activityRule,
                    title = "Same Day (Rp0)",
                    eta = "Estimasi tiba hari ini")
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            hasPassedAnalytics(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithFailedDefaultDuration_PassedAnalyticsAndPaymentIntent() {
        setup(safResponse = R.raw.saf_tokonow_with_failed_default_duration_response)
        activityRule.launchActivity(null)

        checkoutPage {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            // Wait for Validate Use
            waitForData()
            assertHasSingleShipmentSelected(activityRule,
                    title = "Same Day (Rp0)",
                    eta = "Estimasi tiba hari ini")
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            hasPassedAnalytics(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithAdditionalPrice_PassedAnalyticsAndPaymentIntent() {
        setup(ratesResponse = R.raw.ratesv3_tokonow_with_additional_price_response)
        activityRule.launchActivity(null)

        checkoutPage {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            // Wait for Validate Use
            waitForData()
            assertHasSingleShipmentSelected(activityRule,
                    title = "Same Day (",
                    originalPrice = "Rp13.000",
                    discountedPrice = " Rp8.000)",
                    eta = "Estimasi tiba hari ini",
                    message = "Pengiriman melebihi limit bebas ongkir, kamu cukup bayar Rp8.000")
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            hasPassedAnalytics(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithNormalPrice_PassedAnalyticsAndPaymentIntent() {
        setup(ratesResponse = R.raw.ratesv3_tokonow_with_normal_price_response)
        activityRule.launchActivity(null)

        checkoutPage {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            assertHasSingleShipmentSelected(activityRule,
                    title = "Same Day (Rp13.000)",
                    eta = "Estimasi tiba hari ini",
                    message = "Kuota Bebas Ongkirmu habis")
            clickChoosePaymentButton(activityRule)
        } validateAnalytics {
            waitForData()
            hasPassedAnalytics(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
            assertGoToPayment()
        }
    }

    @After
    fun cleanup() {
        gtmLogDBSource.deleteAll().subscribe()
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }

    companion object {
        private const val SHIPMENT_ADDRESS_FORM_KEY = "shipment_address_form"
        private const val SAVE_SHIPMENT_KEY = "save_shipment"
        private const val RATES_V3_KEY = "ratesV3"
        private const val VALIDATE_USE_KEY = "validate_use_promo_revamp"
        private const val CHECKOUT_KEY = "checkout"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/checkout.json"
    }
}