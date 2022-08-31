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
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.buyerorder.detail.revamp.activity.RevampOrderListDetailActivity
import com.tokopedia.buyerorder.setupRemoteConfig
import com.tokopedia.buyerorder.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestUser1
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * created by @bayazidnasir on 31/8/2022
 */

@RunWith(AndroidJUnit4::class)
class RevampOrderListDetailActivitySingleTickerTest {

    private companion object{
        private const val KEY_CONTAINS_ORDER_DETAILS = "orderDetails"
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    val activityRule = object : IntentsTestRule<RevampOrderListDetailActivity>(RevampOrderListDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()
            setupGraphqlMockResponse {
                addMockResponse(
                    KEY_CONTAINS_ORDER_DETAILS,
                    getRawString(context, R.raw.mock_response_oms_single_ticker),
                    FIND_BY_CONTAINS
                )
            }

            setupRemoteConfig(context, true)

            loginInstrumentationTestUser1()
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
                putExtra("order_id", "72b9fd8f-2e86-4484-8577-16cf1d97e16c")
                data = Uri.parse("tokopedia://order/72b9fd8f-2e86-4484-8577-16cf1d97e16c?upstream-ORDERINTERNAL&vertical_category=foodvchr")
            }
        }
    }

    @Test
    fun shouldShowSingleTickerTest(){
        assertLabelTopTicker()
        assertBottomTickerNotShow()
    }

    private fun assertLabelTopTicker() {
        Thread.sleep(1000)
        onView(withId(R.id.status_label)).check(matches(isDisplayed()))
        onView(withId(R.id.status_value)).check(matches(isDisplayed()))
        onView(withId(R.id.status_label)).check(matches(withText("Status")))
        onView(withId(R.id.status_value)).check(matches(withText("Transaksi Dibatalkan")))
        onView(withId(R.id.ticker_status)).check(matches(isDisplayed()))
    }

    private fun assertBottomTickerNotShow(){
        Thread.sleep(1000)
        val scrollView = activityRule.activity.findViewById<NestedScrollView>(R.id.parentScroll)
        while (scrollView.canScrollVertically(1)){
            onView(withId(R.id.parentScroll)).perform(swipeUp())
        }
        Thread.sleep(2000)
        onView(withId(R.id.ticker_detail_order)).check(matches(not(isDisplayed())))
    }

}