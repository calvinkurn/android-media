package com.tokopedia.checkout.testing.journey.analytics

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.checkout.testing.R
import com.tokopedia.checkout.testing.robot.checkoutPage
import com.tokopedia.checkout.view.ShipmentActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckoutAnalyticsTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<ShipmentActivity>(ShipmentActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, R.raw.saf_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.save_shipment_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, R.raw.ratesv3_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.validate_use_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.checkout_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun loadCartAndGoToShipment_PassedAnalyticsTest() {
        activityRule.launchActivity(null)

        checkoutPage {
            waitForData()
            clickChooseDuration(activityRule)
            waitForData()
            selectFirstShippingDurationOption()
            waitForData()
            clickChoosePaymentButton(activityRule)
        } validateAnalytics  {
            waitForData()
            hasPassedAnalytics(cassavaRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }
    }

    @After
    fun cleanup() {
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