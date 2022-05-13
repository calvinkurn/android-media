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
import com.tokopedia.buyerorder.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * created by @bayazidnasir on 17/3/2022
 */
@RunWith(AndroidJUnit4::class)
class OrderListDetailActivityOneTickerTest {

    companion object{
        private const val KEY_CONTAINS_ORDER_DETAILS = "orderDetails"
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    val activityRule: IntentsTestRule<OrderListDetailActivity> =
        object : IntentsTestRule<OrderListDetailActivity>(OrderListDetailActivity::class.java) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                gtmLogDBSource.deleteAll().subscribe()
                setupGraphqlMockResponse {
                    addMockResponse(
                        KEY_CONTAINS_ORDER_DETAILS,
                        InstrumentationMockHelper.getRawString(context, R.raw.mock_response_combined_one_ticker),
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

            override fun getActivityIntent(): Intent {
                return Intent(
                    context,
                    OrderListDetailActivity::class.java
                ).apply {
                    putExtra("order_id", "72b9fd8f-2e86-4484-8577-16cf1d97e16c")
                    data = Uri.parse("tokopedia://order/72b9fd8f-2e86-4484-8577-16cf1d97e16c?upstream-ORDERINTERNAL&vertical_category=foodvchr")
                }
            }
        }

    @Test
    fun shouldShowOneTickerTest(){
        assertLabelAnTopTicker()
        assertBottomTickerNotShow()
    }

    private fun assertLabelAnTopTicker(){
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
