package com.tokopedia.flight.orderdetail.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.RouteManager
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.flight.CustomScrollActions.nestedScrollTo
import com.tokopedia.flight.R
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by furqan on 23/12/2020
 */
@RunWith(AndroidJUnit4::class)
class FlightOrderDetailActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<FlightOrderDetailActivity> = object : IntentsTestRule<FlightOrderDetailActivity>(FlightOrderDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(FlightOrderDetailMockResponse())
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
                RouteManager.getIntent(context, "tokopedia://pesawat/order/DUMMY_ORDER_ID")
    }

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun validateTrackingOrderDetail() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Thread.sleep(1000)

        onView(withId(R.id.tgFlightOrderTerminalNote)).perform(nestedScrollTo())
        onView(withId(R.id.btnFlightOrderDetailSendEticket)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.bottom_sheet_close)).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.containerContentOrderDetail)).perform(swipeUp())
        onView(withText("Web Check-in")).perform(click())
        onView(withText("Batalkan Tiket")).perform(click())
        Thread.sleep(2000)

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_ORDER_DETAIL),
                hasAllSuccess())
    }

    @Test
    fun validateTrackingWebCheckIn() {
        Thread.sleep(1000)

        onView(withId(R.id.containerContentOrderDetail)).perform(swipeUp())
        onView(withText("Web Check-in")).perform(click())
        Thread.sleep(1000)

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(1000)

        onView(getElementFromMatchAtPosition(withId(R.id.btnFlightOrderDetailWebCheckIn), 0)).perform(click())
        onView(withId(R.id.rvFlightOrderDetailWebCheckIn)).perform(swipeUp())
        onView(getElementFromMatchAtPosition(withId(R.id.btnFlightOrderDetailWebCheckIn), 1)).perform(click())
        Thread.sleep(1000)

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_ORDER_DETAIL_WEB_CHECK_IN),
                hasAllSuccess())
    }

    @Test
    fun validateTrackingDownload() {
        Thread.sleep(1000)

        onView(withId(R.id.tgFlightOrderTerminalNote)).perform(nestedScrollTo())
        onView(withId(R.id.btnFlightOrderDetailViewEticket)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.menu_webview_download)).perform(click())
        Thread.sleep(1000)

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_ORDER_DETAIL_BROWSER),
                hasAllSuccess())
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_ORDER_DETAIL = "tracker/travel/flight/flight_order_detail.json"
        private const val ANALYTIC_VALIDATOR_QUERY_ORDER_DETAIL_BROWSER = "tracker/travel/flight/flight_order_detail_browser.json"
        private const val ANALYTIC_VALIDATOR_QUERY_ORDER_DETAIL_WEB_CHECK_IN = "tracker/travel/flight/flight_order_detail_web_check_in.json"

    }
}