package com.tokopedia.cart.journey.analytics

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cart.view.CartActivity
import com.tokopedia.cart.view.CartIdlingResource
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.cart.test.R
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartAnalyticsTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<CartActivity>(CartActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var idlingResource: IdlingResource? = null

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun setup() {
        idlingResource = CartIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
        setupGraphqlMockResponse {
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, R.raw.cart_bundle_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(UPDATE_CART_KEY, InstrumentationMockHelper.getRawString(context, R.raw.update_cart_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun loadCartAndGoToShipment_PassedAnalyticsTest() {
        activityRule.launchActivity(null)

        cartPage {
            clickBuyButton()
            waitForData()
        } validateAnalytics  {
            hasPassedAnalytics(cassavaRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }

        // Prevent glide crash
        Thread.sleep(2000)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }

    companion object {
        private const val GET_CART_LIST_KEY = "cart_revamp_v3"
        private const val UPDATE_CART_KEY = "update_cart_v2"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/cart.json"
    }

}