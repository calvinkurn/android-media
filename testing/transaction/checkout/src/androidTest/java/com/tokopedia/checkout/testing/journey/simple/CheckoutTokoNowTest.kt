package com.tokopedia.checkout.testing.journey.simple

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.CHECKOUT_DEFAULT_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.CHECKOUT_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.RATES_V3_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.RATES_V3_TOKONOW_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.RATES_V3_TOKONOW_WITH_ADDITIONAL_PRICE_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.RATES_V3_TOKONOW_WITH_NORMAL_PRICE_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SAVE_SHIPMENT_DEFAULT_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SAVE_SHIPMENT_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SHIPMENT_ADDRESS_FORM_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SHIPMENT_ADDRESS_FORM_TOKONOW_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SHIPMENT_ADDRESS_FORM_TOKONOW_WITH_FAILED_DEFAULT_DURATION_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.VALIDATE_USE_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.VALIDATE_USE_TOKONOW_RESPONSE
import com.tokopedia.checkout.testing.robot.checkoutPage
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

    private fun setup(safResponse: Int = SHIPMENT_ADDRESS_FORM_TOKONOW_RESPONSE, ratesResponse: Int = RATES_V3_TOKONOW_RESPONSE) {
        setupGraphqlMockResponse {
            addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, safResponse), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, SAVE_SHIPMENT_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, ratesResponse), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, VALIDATE_USE_TOKONOW_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, CHECKOUT_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
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
            assertHasSingleShipmentSelected(
                    title = "NOW! (Rp0)",
                    eta = "Estimasi tiba hari ini")
            clickChoosePaymentButton()
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithFailedDefaultDuration_PassedAnalyticsAndPaymentIntent() {
        setup(safResponse = SHIPMENT_ADDRESS_FORM_TOKONOW_WITH_FAILED_DEFAULT_DURATION_RESPONSE)
        activityRule.launchActivity(null)

        checkoutPage {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            // Wait for Validate Use
            waitForData()
            assertHasSingleShipmentSelected(
                    title = "NOW! (Rp0)",
                    eta = "Estimasi tiba hari ini")
            clickChoosePaymentButton()
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithAdditionalPrice_PassedAnalyticsAndPaymentIntent() {
        setup(ratesResponse = RATES_V3_TOKONOW_WITH_ADDITIONAL_PRICE_RESPONSE)
        activityRule.launchActivity(null)

        checkoutPage {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            // Wait for Validate Use
            waitForData()
            assertHasSingleShipmentSelected(
                    title = "NOW! (",
                    originalPrice = "Rp13.000",
                    discountedPrice = " Rp8.000)",
                    eta = "Estimasi tiba hari ini",
                    message = "Pengiriman melebihi limit bebas ongkir, kamu cukup bayar Rp8.000")
            clickChoosePaymentButton()
        } validateAnalytics {
            waitForData()
            assertGoToPayment()
        }
    }

    @Test
    fun tokoNowWithNormalPrice_PassedAnalyticsAndPaymentIntent() {
        setup(ratesResponse = RATES_V3_TOKONOW_WITH_NORMAL_PRICE_RESPONSE)
        activityRule.launchActivity(null)

        checkoutPage {
            // Wait for SAF
            waitForData()
            // Wait for Rates
            waitForData()
            assertHasSingleShipmentSelected(
                    title = "NOW! (Rp13.000)",
                    eta = "Estimasi tiba hari ini",
                    message = "Kuota Bebas Ongkirmu habis")
            clickChoosePaymentButton()
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