package com.tokopedia.buyerorder.detail.view.activity

import android.content.Intent
import android.net.Uri
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.buyerorder.IdlingResourceTestRule
import com.tokopedia.buyerorder.KEY_CONTAINS_ORDER_DETAILS
import com.tokopedia.buyerorder.ORDER_DETAIL_APPLINK
import com.tokopedia.buyerorder.ORDER_ID_KEY
import com.tokopedia.buyerorder.ORDER_ID_VALUE
import com.tokopedia.buyerorder.common.idling.OmsIdlingResource
import com.tokopedia.buyerorder.detail.view.activity.environment.InstrumentTestOrderListDetailActivity
import com.tokopedia.buyerorder.setupRemoteConfig
import com.tokopedia.buyerorder.test.R
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * created by @bayazidnasir on 17/3/2022
 */

@UiTest
class OrderListDetailActivityTwoTickerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val idlingResourceRule = IdlingResourceTestRule()

    @get:Rule
    val activityRule: IntentsTestRule<InstrumentTestOrderListDetailActivity> =
        object : IntentsTestRule<InstrumentTestOrderListDetailActivity>(InstrumentTestOrderListDetailActivity::class.java) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                setupGraphqlMockResponse {
                    addMockResponse(
                        KEY_CONTAINS_ORDER_DETAILS,
                        InstrumentationMockHelper.getRawString(context, R.raw.mock_response_combined_two_ticker),
                        MockModelConfig.FIND_BY_CONTAINS
                    )
                }

                setupRemoteConfig(context, false)

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
                    InstrumentTestOrderListDetailActivity::class.java
                ).apply {
                    putExtra(ORDER_ID_KEY, ORDER_ID_VALUE)
                    data = Uri.parse(ORDER_DETAIL_APPLINK)
                }
            }
        }

    private var idlingResource: IdlingResource? = null

    @Before
    fun setup() {
        idlingResource = OmsIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun shouldShowTwoTickerTest(){
        assertLabelAnTopTicker()
        assertBottomTickerShow()
    }

    private fun assertLabelAnTopTicker(){
        onView(ViewMatchers.withId(R.id.status_label))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.status_value))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(ViewMatchers.withId(R.id.status_label))
            .check(ViewAssertions.matches(ViewMatchers.withText("Status")))
        onView(ViewMatchers.withId(R.id.status_value))
            .check(ViewAssertions.matches(ViewMatchers.withText("Transaksi Dibatalkan")))

        onView(ViewMatchers.withId(R.id.ticker_status))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    private fun assertBottomTickerShow(){
        val scrollView = activityRule.activity.findViewById<NestedScrollView>(R.id.parentScroll)

        while (scrollView.canScrollVertically(1)){
            onView(ViewMatchers.withId(R.id.parentScroll)).perform(ViewActions.swipeUp())
        }
        onView(ViewMatchers.withId(R.id.ticker_detail_order))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}