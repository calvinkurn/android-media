package com.tokopedia.buyerorder.detail.view.activity

import android.content.Intent
import android.net.Uri
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.buyerorder.KEY_CONTAINS_ORDER_DETAILS
import com.tokopedia.buyerorder.ORDER_DETAIL_APPLINK
import com.tokopedia.buyerorder.ORDER_ID_KEY
import com.tokopedia.buyerorder.ORDER_ID_VALUE
import com.tokopedia.buyerorder.detail.revamp.activity.RevampOrderListDetailActivity
import com.tokopedia.buyerorder.disableCoachMark
import com.tokopedia.buyerorder.setupRemoteConfig
import com.tokopedia.buyerorder.test.R
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.Rule
import org.junit.Test

/**
 * created by @bayazidnasir on 31/8/2022
 */

@UiTest
class RevampOrderListDetailActivityDoubleTicker {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val activityRule = object : IntentsTestRule<RevampOrderListDetailActivity>(
        RevampOrderListDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse {
                addMockResponse(
                    KEY_CONTAINS_ORDER_DETAILS,
                    InstrumentationMockHelper.getRawString(context, R.raw.mock_response_oms_double_ticker),
                    MockModelConfig.FIND_BY_CONTAINS
                )
            }

            setupRemoteConfig(context, true)
            disableCoachMark(context)

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

        override fun getActivityIntent(): Intent {
            return Intent(
                context,
                RevampOrderListDetailActivity::class.java
            ).apply {
                putExtra(ORDER_ID_KEY, ORDER_ID_VALUE)
                data = Uri.parse(ORDER_DETAIL_APPLINK)
            }
        }
    }

    @Test
    fun shouldShowDoubleTickerTest(){
        assertLabelTopTicker()
        assertBottomTickerShow()
    }

    private fun assertLabelTopTicker() {
        Thread.sleep(1000)
        onView(withId(R.id.status_label)).check(matches(isDisplayed()))
        onView(withId(R.id.status_value)).check(matches(isDisplayed()))
        onView(withId(R.id.status_label)).check(matches(withText("Status")))
        onView(withId(R.id.status_value)).check(matches(withText("Transaksi Dibatalkan")))
        onView(withId(R.id.ticker_status)).check(matches(isDisplayed()))
    }

    private fun assertBottomTickerShow(){
        Thread.sleep(1000)
        val scrollView = activityRule.activity.findViewById<NestedScrollView>(R.id.parentScroll)
        while (scrollView.canScrollVertically(1)){
            onView(withId(R.id.parentScroll)).perform(swipeUp())
        }
        Thread.sleep(2000)
        onView(withId(R.id.ticker_detail_order)).check(matches(isDisplayed()))
    }
}