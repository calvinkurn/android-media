package com.tokopedia.checkout.journey.simple

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.checkout.ShipmentActivity
import com.tokopedia.checkout.robot.checkoutPage
import com.tokopedia.checkout.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Rule
import org.junit.Test

class CheckoutOwocTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<ShipmentActivity>(ShipmentActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // still need mock gql response using interceptor because use ratesV3 usecase which recreate GraphqlUseCase for every hit
    private fun setup(safResponse: Int = R.raw.saf_bundle_tokonow_default_response, ratesResponse: Int = R.raw.ratesv3_tokonow_default_response) {
        setupGraphqlMockResponse {
            addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, safResponse), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.save_shipment_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, ratesResponse), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.validate_use_tokonow_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(PAYMENT_FEE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.payment_fee_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.checkout_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun newUiGroupType() {
        setup(safResponse = R.raw.saf_owoc_default_response)
        activityRule.launchActivity(null)

        checkoutPage {
            // Wait for SAF
            waitForData()
            assertNewUiGroupType(activityRule, 0)
            assertNewUiGroupType(activityRule, 1)
            clickChooseDuration(activityRule)
            waitForData()
            selectFirstShippingDurationOption()
            waitForData()
            scrollRecyclerViewToChoosePaymentButton(activityRule)
            waitForData()
            waitForData()
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

    companion object {
        private const val SHIPMENT_ADDRESS_FORM_KEY = "shipmentAddressFormV4"
        private const val SAVE_SHIPMENT_KEY = "save_shipment"
        private const val RATES_V3_KEY = "ratesV3"
        private const val VALIDATE_USE_KEY = "validate_use_promo_revamp"
        private const val PAYMENT_FEE_KEY = "getPaymentFeeCheckout"
        private const val CHECKOUT_KEY = "checkout"
    }
}
