package com.tokopedia.cart.journey.analytics

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.cart.test.R
import com.tokopedia.cart.view.CartActivity
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
    var activityRule = object : IntentsTestRule<CartActivity>(CartActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, R.raw.cart_analytics_promo_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(UPDATE_CART_KEY, InstrumentationMockHelper.getRawString(context, R.raw.update_cart_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(VALIDATE_USE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.validate_use_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
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
            hasPassedAnalytics(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }

        // Prevent glide crash
        Thread.sleep(2000)
    }

    @After
    fun cleanup() {
        gtmLogDBSource.deleteAll().subscribe()
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }

    companion object {
        private const val GET_CART_LIST_KEY = "cart_revamp"
        private const val UPDATE_CART_KEY = "update_cart_v2"
        private const val VALIDATE_USE_KEY = "validate_use_promo_revamp"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/cart_promo.json"
    }

}