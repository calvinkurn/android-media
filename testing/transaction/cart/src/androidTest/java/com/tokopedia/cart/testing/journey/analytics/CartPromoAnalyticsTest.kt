package com.tokopedia.cart.testing.journey.analytics

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.testing.robot.CartPageIntentTestRule
import com.tokopedia.cart.testing.robot.CartPageMocks.GET_CART_LIST_KEY
import com.tokopedia.cart.testing.robot.CartPageMocks.GET_CART_LIST_MOCK_PROMO_RESPONSE
import com.tokopedia.cart.testing.robot.CartPageMocks.UPDATE_CART_KEY
import com.tokopedia.cart.testing.robot.CartPageMocks.UPDATE_CART_MOCK_DEFAULT_RESPONSE
import com.tokopedia.cart.testing.robot.CartPageMocks.VALIDATE_USE_KEY
import com.tokopedia.cart.testing.robot.CartPageMocks.VALIDATE_USE_MOCK_DEFAULT_RESPONSE
import com.tokopedia.cart.testing.robot.cartPage
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartPromoAnalyticsTest {

    @get:Rule
    var activityRule = object : CartPageIntentTestRule(false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, GET_CART_LIST_MOCK_PROMO_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(UPDATE_CART_KEY, InstrumentationMockHelper.getRawString(context, UPDATE_CART_MOCK_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, VALIDATE_USE_MOCK_DEFAULT_RESPONSE), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun cartPromoJourney_PassedAnalyticsTest() {
        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))

        cartPage {
            waitForData()
            clickPromoButton()
        } validateAnalytics {
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }

        // Prevent glide crash
        Thread.sleep(2000)
    }

    @After
    fun cleanup() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/cart_promo.json"
    }

}