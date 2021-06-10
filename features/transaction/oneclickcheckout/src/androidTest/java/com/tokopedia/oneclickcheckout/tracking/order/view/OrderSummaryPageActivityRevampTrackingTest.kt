package com.tokopedia.oneclickcheckout.tracking.order.view

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.oneclickcheckout.order.view.TestOrderSummaryPageActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OrderSummaryPageActivityRevampTrackingTest {

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/transaction/one_click_checkout_revamp.json"
    }

    @get:Rule
    var activityRule = IntentsTestRule(TestOrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

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

        gtmLogDBSource.deleteAll().subscribe()
        OneClickCheckoutInterceptor.resetAllCustomResponse()

        OneClickCheckoutInterceptor.setupGraphqlMockResponse(context)
        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        gtmLogDBSource.deleteAll().subscribe()
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun performRevampAnalyticsActions() {
        cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH
        activityRule.launchActivity(null)

        intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
        //    Deprecated Test (will delete in next iteration)
//            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_ONE_PROFILE_REVAMP_RESPONSE_PATH
//            clickAddPreferenceForNewBuyer()
//
//            cartInterceptor.customGetOccCartResponsePath = GET_OCC_CART_PAGE_MANY_PROFILE_REVAMP_RESPONSE_PATH
//            clickAddOrChangePreferenceRevamp(null)
//
//            clickAddOrChangePreferenceRevamp {
//                clickEditPreference(1)
//            }
//
//            clickAddOrChangePreferenceRevamp {
//                clickUsePreferenceRevamp(1)
//            }

            clickChangeAddressRevamp()
            closeBottomSheet()

            clickChangeDurationRevamp {
                chooseDurationWithText("Next Day (1 hari)")
            }

            clickChangePaymentRevamp()
        }

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME), hasAllSuccess())
    }
}