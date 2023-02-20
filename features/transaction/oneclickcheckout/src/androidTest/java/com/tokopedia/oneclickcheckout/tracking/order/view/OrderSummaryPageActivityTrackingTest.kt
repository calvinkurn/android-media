package com.tokopedia.oneclickcheckout.tracking.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.CHECKOUT_PRICE_CHANGE_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_LAST_APPLY_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.interceptor.VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
import com.tokopedia.oneclickcheckout.common.interceptor.VALIDATE_USE_PROMO_REVAMP_CASHBACK_FULL_APPLIED_RESPONSE
import com.tokopedia.oneclickcheckout.common.interceptor.VALIDATE_USE_PROMO_REVAMP_CASHBACK_HALF_APPLIED_RESPONSE
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageActivity
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@CassavaTest
class OrderSummaryPageActivityTrackingTest {

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/one_click_checkout_order_summary.json"
        private const val ANALYTIC_VALIDATOR_PROMO_RED_STATE_QUERY_FILE_NAME = "tracker/transaction/one_click_checkout_order_summary_promo_red_state.json"
    }

    @get:Rule
    var activityRule = IntentsTestRule(OrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    @get:Rule
    val cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val promoInterceptor = OneClickCheckoutInterceptor.promoInterceptor
    private val checkoutInterceptor = OneClickCheckoutInterceptor.checkoutInterceptor

    @Before
    fun setup() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        OneClickCheckoutInterceptor.resetAllCustomResponse()

        OneClickCheckoutInterceptor.setupGraphqlMockResponse(context)
        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun performOrderSummaryPageTrackingActions() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH
        activityRule.launchActivity(null)

        performOrderSummaryPageBackAction()

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            clickButtonPromo()

            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_LAST_APPLY_REVAMP_RESPONSE_PATH
            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_CASHBACK_FULL_APPLIED_RESPONSE

            clickChangeAddressRevamp {
                clickAddress(1)
            }

            clickChangeCourierRevamp {
                promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_CASHBACK_HALF_APPLIED_RESPONSE
                chooseCourierWithText("AnterAja")
            }

            // wait for bottom sheet to fully close
            Thread.sleep(2000)
            clickButtonPromo()

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
            clickApplyShipmentPromoRevamp()

            clickButtonPromo()

            checkoutInterceptor.customCheckoutResponsePath = CHECKOUT_PRICE_CHANGE_RESPONSE_PATH
            pay()
            clickDialogPrimaryButton()

            checkoutInterceptor.customCheckoutResponsePath = null
            pay()
        }

        assertThat(cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
        activityRule.activity.finishAndRemoveTask()
    }

    private fun performOrderSummaryPageBackAction() {
        // prevent press back on non-root activity
        val activity = activityRule.activity
        activity.startActivity(Intent(activity, OrderSummaryPageActivity::class.java))
        Espresso.pressBack()
    }
}
