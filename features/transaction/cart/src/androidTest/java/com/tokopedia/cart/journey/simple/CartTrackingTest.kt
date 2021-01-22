package com.tokopedia.cart.journey.simple

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cart.InstrumentTestCartActivity
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.cart.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartTrackingTest {

    @get:Rule
    var activityRule = IntentsTestRule(InstrumentTestCartActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, R.raw.cart_tracking_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(UPDATE_CART_KEY, InstrumentationMockHelper.getRawString(context, R.raw.update_cart_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun loadCartAndGoToShipment_PassedAnalyticsTest() {
        activityRule.launchActivity(null)

        cartPage {
            Thread.sleep(1000)
        } buy {
            hasPassedAnalytics(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }

    }

    @After
    fun cleanup() {
        gtmLogDBSource.deleteAll().subscribe()
        if (!activityRule.activity.isDestroyed) activityRule.finishActivity()
    }

    companion object {
        private const val GET_CART_LIST_KEY = "cart_revamp"
        private const val UPDATE_CART_KEY = "update_cart_v2"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/cart.json"
    }

}