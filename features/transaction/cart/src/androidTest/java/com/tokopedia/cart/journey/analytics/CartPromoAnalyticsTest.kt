package com.tokopedia.cart.journey.analytics

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.cart.CartActivity
import com.tokopedia.cart.robot.cartPage
import com.tokopedia.cart.view.CartIdlingResource
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.cart.test.R as carttestR

@CassavaTest
class CartPromoAnalyticsTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<CartActivity>(CartActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var idlingResource: IdlingResource? = null

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Before
    fun setup() {
        idlingResource = CartIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
        setupGraphqlMockResponse {
            addMockResponse(GET_CART_LIST_KEY, InstrumentationMockHelper.getRawString(context, carttestR.raw.cart_bundle_analytics_promo_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(UPDATE_CART_KEY, InstrumentationMockHelper.getRawString(context, carttestR.raw.update_cart_response), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(GET_LAST_APPLY_PROMO_KEY, InstrumentationMockHelper.getRawString(context, carttestR.raw.get_last_apply_analytics_default_response), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @Test
    fun cartPromoJourney_PassedAnalyticsTest() {
        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))

        cartPage {
            clickPromoButton()
        } validateAnalytics {
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
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
        private const val GET_CART_LIST_KEY = "cart_revamp_v4"
        private const val UPDATE_CART_KEY = "update_cart_v2"
        private const val GET_LAST_APPLY_PROMO_KEY = "get_last_apply_promo"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/cart_promo.json"
    }
}
