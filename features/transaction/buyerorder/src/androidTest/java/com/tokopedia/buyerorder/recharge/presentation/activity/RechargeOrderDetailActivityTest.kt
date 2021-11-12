package com.tokopedia.buyerorder.recharge.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.R
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by furqan on 10/11/2021
 */
@RunWith(AndroidJUnit4::class)
class RechargeOrderDetailActivityTest {

    @get:Rule
    val activityRule = object : IntentsTestRule<RechargeOrderDetailActivity>(RechargeOrderDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()
            setupGraphqlMockResponse {
                addMockResponse(
                        KEY_CONTAINS_ORDER_DETAILS,
                        InstrumentationMockHelper.getRawString(context, com.tokopedia.buyerorder.test.R.raw.mock_response_recharge_order_detail),
                        MockModelConfig.FIND_BY_CONTAINS
                )
                addMockResponse(
                        KEY_CONTAINS_RECOMMENDATION_SKELETON,
                        InstrumentationMockHelper.getRawString(context, com.tokopedia.buyerorder.test.R.raw.mock_response_recharge_recommendation_skeleton),
                        MockModelConfig.FIND_BY_CONTAINS
                )
                addMockResponse(
                        KEY_CONTAINS_DIGITAL_RECOMMENDATION,
                        InstrumentationMockHelper.getRawString(context, com.tokopedia.buyerorder.test.R.raw.mock_response_recharge_digital_recommendation),
                        MockModelConfig.FIND_BY_CONTAINS
                )
            }
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            val userSession = UserSession(context)
            userSession.setLoginSession(
                    true,
                    userSession.userId,
                    userSession.name,
                    userSession.shopId,
                    true,
                    userSession.shopName,
                    userSession.email,
                    userSession.isGoldMerchant,
                    userSession.phoneNumber
            )
        }

        override fun getActivityIntent(): Intent =
                createIntent()
    }

    @get:Rule
    val cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Test
    fun validateAnalyticsOrderDetail() {
        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(3000)

        actionInvoice()
        Thread.sleep(1000)
        // click primary button
        onView(withId(R.id.btn_recharge_order_detail_sticky)).perform(click())
        Thread.sleep(1000)
        actionSwipeUp()
        actionSwipeUp()
        actionClickOnActionButtons()

        Thread.sleep(3000)

        assertThat(cassavaTestRule.validate(CASSAVA_ORDER_DETAIL_QUERY), hasAllSuccess())
    }

    private fun actionInvoice() {
        onView(withId(R.id.tg_recharge_see_invoice)).perform(click())
        onView(withId(R.id.ic_recharge_invoice_copy)).perform(click())
    }

    private fun actionSwipeUp() {
        onView(withId(R.id.rv_recharge_order_detail)).perform(swipeUp())
    }

    private fun actionClickOnActionButtons() {
        // click feature button
        onView(withText("Langganan")).perform(click())
        Thread.sleep(1000)
        // click secondary button
        onView(withText("Secondary Button")).perform(click())
        Thread.sleep(1000)
    }

    private fun createIntent(): Intent =
            Intent(InstrumentationRegistry.getInstrumentation().targetContext,
                    RechargeOrderDetailActivity::class.java).apply {
                data = Uri.parse("tokopedia://digital/order/921262126")
                putExtra("order_id", "921262126")
            }

    companion object {
        private const val KEY_CONTAINS_ORDER_DETAILS = "orderDetails"
        private const val KEY_CONTAINS_RECOMMENDATION_SKELETON = "\"channelName\": \"dg_od_skeleton\""
        private const val KEY_CONTAINS_DIGITAL_RECOMMENDATION = "\"channelName\": \"dg_order_detail\""

        private const val CASSAVA_ORDER_DETAIL_QUERY = "tracker/recharge/order_detail/recharge_order_detail.json"
    }
}