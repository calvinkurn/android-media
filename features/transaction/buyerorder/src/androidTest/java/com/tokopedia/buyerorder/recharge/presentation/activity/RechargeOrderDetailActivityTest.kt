package com.tokopedia.buyerorder.recharge.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.R
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.After
import org.junit.Before
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

    @Before
    fun setUp() {
        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun validateAnalyticsOrderDetail() {
        Thread.sleep(3000)

        onView(withId(R.id.rv_recharge_order_detail)).perform(swipeUp())
        Thread.sleep(3000)
        onView(withId(R.id.rv_recharge_order_detail)).perform(swipeUp())
        Thread.sleep(3000)
        onView(withId(R.id.rv_recharge_order_detail)).perform(swipeUp())
        Thread.sleep(3000)
        onView(withId(R.id.rv_recharge_order_detail)).perform(swipeUp())
        Thread.sleep(3000)
    }

    private fun createIntent(): Intent =
            Intent(InstrumentationRegistry.getInstrumentation().targetContext,
                    RechargeOrderDetailActivity::class.java).apply {
                data = Uri.parse("tokopedia://digital/order/921262126")
                putExtra("order_id", "921262126")
            }

    companion object {
        private const val KEY_CONTAINS_ORDER_DETAILS = "orderDetails"
    }
}