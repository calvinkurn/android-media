package com.tokopedia.checkout.testing.journey.analytics

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.CHECKOUT_DEFAULT_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.CHECKOUT_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.RATES_V3_DEFAULT_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.RATES_V3_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SAVE_SHIPMENT_DEFAULT_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SAVE_SHIPMENT_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SHIPMENT_ADDRESS_FORM_DEFAULT_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.SHIPMENT_ADDRESS_FORM_KEY
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.VALIDATE_USE_DEFAULT_RESPONSE
import com.tokopedia.checkout.testing.robot.CheckoutPageMocks.VALIDATE_USE_KEY
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
            addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, SHIPMENT_ADDRESS_FORM_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(SAVE_SHIPMENT_KEY, InstrumentationMockHelper.getRawString(context, SAVE_SHIPMENT_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(RATES_V3_KEY, InstrumentationMockHelper.getRawString(context, RATES_V3_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, VALIDATE_USE_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(CHECKOUT_KEY, InstrumentationMockHelper.getRawString(context, CHECKOUT_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun loadCartAndGoToShipment_PassedAnalyticsTest() {
        activityRule.launchActivity(null)

        checkoutPage {
            waitForData()
            clickChooseDuration()
            waitForData()
            selectFirstShippingDurationOption()
            waitForData()
            clickChoosePaymentButton()
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
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/checkout.json"
    }

}