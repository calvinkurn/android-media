package com.tokopedia.oneclickcheckout.tracking.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.*
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityTrackingTest {

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/one_click_checkout_order_summary.json"
    }

    @get:Rule
    var activityRule = IntentsTestRule(OrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    @get:Rule
    val cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val gtmLogDBSource = GtmLogDBSource(context)

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val preferenceInterceptor = OneClickCheckoutInterceptor.preferenceInterceptor
    private val logisticInterceptor = OneClickCheckoutInterceptor.logisticInterceptor
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
        activityRule.finishActivity()
    }

    @Test
    fun performOrderSummaryPageTrackingActions() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH
        activityRule.launchActivity(null)

        performOrderSummaryPageBackAction()

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
//            clickOnboardingInfo()
//            closeBottomSheet()
//
//            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH
//            clickAddPreferenceForNewBuyer()

            clickButtonPromo()

//            clickAddOrChangePreferenceRevamp {
//                clickAddPreference()
//            }

            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_LAST_APPLY_REVAMP_RESPONSE_PATH
            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_CASHBACK_FULL_APPLIED_RESPONSE

            clickChangeAddressRevamp {
                clickAddress(1)
            }

//            clickAddOrChangePreferenceRevamp {
//                clickEditPreference(1)
//            }
//
//            clickAddOrChangePreferenceRevamp {
//                clickUsePreference(1)
//            }

            clickChangeCourierRevamp {
                promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_CASHBACK_HALF_APPLIED_RESPONSE
                chooseCourierWithText("AnterAja")
            }

            clickButtonPromo()

            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE
            clickApplyShipmentPromoRevamp()

            clickButtonPromo()

            checkoutInterceptor.customCheckoutResponsePath = CHECKOUT_EMPTY_STOCK_RESPONSE_PATH
            pay()
            closeBottomSheet()

            checkoutInterceptor.customCheckoutResponsePath = null
            pay()
        }

        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_LAST_APPLY_WITH_LOW_MAXIMUM_PAYMENT_REVAMP_RESPONSE_PATH
        promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_CASHBACK_FULL_APPLIED_RESPONSE
        Intents.release()
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            promoInterceptor.customValidateUseResponsePath = VALIDATE_USE_PROMO_REVAMP_CASHBACK_RED_STATE_RESPONSE
            clickAddProductQuantity()

            clickButtonPromo()

            checkoutInterceptor.customCheckoutResponsePath = CHECKOUT_EMPTY_STOCK_RESPONSE_PATH
            pay()
            clickButtonContinueWithRedPromo()
            closeBottomSheet()
            closeBottomSheet()

            checkoutInterceptor.customCheckoutResponsePath = null
            pay()
            clickButtonContinueWithRedPromo()
        }

        assertThat(cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }

    private fun performOrderSummaryPageBackAction() {
        // prevent press back on non-root activity
        val activity = activityRule.activity
        activity.startActivity(Intent(activity, OrderSummaryPageActivity::class.java))
        Espresso.pressBack()
    }
}