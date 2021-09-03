package com.tokopedia.checkout.testing.journey.analytics

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
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

class CheckoutPromoAnalyticsTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<ShipmentActivity>(ShipmentActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(SHIPMENT_ADDRESS_FORM_KEY, InstrumentationMockHelper.getRawString(context, R.raw.saf_analytics_promo_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun checkoutPromoJourney_PassedAnalyticsTest() {
        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))

        checkoutPage {
            waitForData()
            clickPromoButton(activityRule)
        } validateAnalytics  {
            hasPassedAnalytics(cassavaRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }

    }

    @After
    fun cleanup() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }

    companion object {
        private const val SHIPMENT_ADDRESS_FORM_KEY = "shipment_address_form"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/checkout_promo.json"
    }

}