package com.tokopedia.hotel.booking.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.activity.mock.HotelBookingMockResponseConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.*

/**
 * @author by jessica on 22/09/20
 */
class HotelBookingActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<HotelBookingActivity> = object : IntentsTestRule<HotelBookingActivity>(HotelBookingActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()
            setupGraphqlMockResponse(HotelBookingMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return HotelBookingActivity.getCallingIntent(context, "", "region", "Hotel A", 1, 2)
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun testBookingFlow() {
        Thread.sleep(2000)
        login()

        Thread.sleep(2000)
        Espresso.onView(withId(R.id.hotel_booking_container)).perform(swipeUp())

        Thread.sleep(2000)
        Espresso.onView(withId(R.id.booking_button)).perform(click())

        Thread.sleep(2000)
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_HOTEL_BOOKING_PAGE),
                hasAllSuccess())
    }

    private fun login() {
        Thread.sleep(3000)
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_HOTEL_BOOKING_PAGE = "tracker/travel/hotel/hotel_booking.json"
    }
}