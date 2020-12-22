package com.tokopedia.flight.homepage.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.flight.R
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by furqan on 04/08/2020
 */
@RunWith(AndroidJUnit4::class)
class FlightHomepageActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<FlightHomepageActivity> = object : IntentsTestRule<FlightHomepageActivity>(FlightHomepageActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(FlightHomepageMockResponse())
        }

        override fun getActivityIntent(): Intent =
                Intent(context, FlightHomepageActivity::class.java)
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validateFlightHomepageP1Tracking() {
        Thread.sleep(3000)
        onView(withId(R.id.nsvFlightHomepage)).perform(swipeUp())
        Thread.sleep(1000)
        onView(withId(R.id.nsvFlightHomepage)).perform(swipeUp())

        validateFlightHomepageBannerDisplayedAndScrollable()
        validateFlightHomepageBannerClickableAndPerformClick()
        validateFlightHomepageSearchClick()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_P1),
                hasAllSuccess())
    }

    private fun validateFlightHomepageSearchClick() {
        Thread.sleep(2000)
        onView(withId(R.id.btnFlightSearch)).perform(click())
    }

    private fun validateFlightHomepageBannerDisplayedAndScrollable() {
        Thread.sleep(2000)
        if (getBannerItemCount() > 0) {
            onView(withId(R.id.banner_recyclerview)).check(matches(isDisplayed()))
            Thread.sleep(1000)
            if (getBannerItemCount() > 1)
                onView(withId(R.id.banner_recyclerview))
                        .perform(RecyclerViewActions.scrollToPosition<BannerViewPagerAdapter.BannerViewHolder>(
                                getBannerItemCount() - 1))
        } else {
            Thread.sleep(1000)
            onView(withId(R.id.banner_recyclerview)).check(matches(Matchers.not(isDisplayed())))
        }
    }

    private fun validateFlightHomepageBannerClickableAndPerformClick() {
        Thread.sleep(2000)

        if (getBannerItemCount() > 0) {
            onView(withId(R.id.banner_recyclerview)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<BannerViewPagerAdapter.BannerViewHolder>(0, click()))
        }
    }

    private fun getBannerItemCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.banner_recyclerview) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    @Test
    fun validateFlightHomepageAnalyticsP2AndBelow() {
        onView(withId(R.id.nsvFlightHomepage)).perform(swipeDown())

        departureAirport()
        arrivalAirport()
        switchTrip()
        setPassengersCount()
        setPassengersClass()

        Thread.sleep(1000)
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_ALL),
                hasAllSuccess())
    }

    private fun departureAirport() {
        Thread.sleep(1000)

        // click on flight departure airport to open bottom sheet to select airport
        onView(withId(R.id.tvFlightOriginAirport)).perform(click())

        // click on Padang, to set Padang as Departure Airport
        onView(withText("Padang, Indonesia")).perform(click())
        Thread.sleep(1000)
    }

    private fun arrivalAirport() {
        Thread.sleep(1000)

        // click on flight arrival airport to open bottom sheet to select airport
        onView(withId(R.id.tvFlightDestinationAirport)).perform(click())

        // click on Palembang, to set Palembang as Arrival Airport
        onView(withText("Palembang, Indonesia")).perform(click())
        Thread.sleep(1000)
    }

    private fun switchTrip() {
        Thread.sleep(1000)

        // click switch to switch between one way and round trip
        onView(withId(R.id.switchFlightRoundTrip)).perform(click())

        Thread.sleep(1000)
    }

    private fun setPassengersCount() {
        Thread.sleep(1000)

        // click on flight passengers to open bottom sheet to set passengers number
        onView(withId(R.id.tvFlightPassenger)).perform(click())

        // set passengers, 3 adult, 2 child, 1 infant
        onView(getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 0)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 0)).perform(click())

        onView(getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 1)).perform(click())
        onView(getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 1)).perform(click())

        onView(getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 2)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.btnFlightPassenger)).perform(click())
        Thread.sleep(1000)
    }

    private fun setPassengersClass() {
        Thread.sleep(1000)

        // click on flight class to open bottom sheet to set passengers class
        onView(withId(R.id.tvFlightClass)).perform(click())

        // set class, Bisnis
        onView(withId(R.id.radioBisnisClass)).perform(click())
        Thread.sleep(1000)
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_P1 = "tracker/travel/flight/flight_homepage_p1.json"
        private const val ANALYTIC_VALIDATOR_QUERY_ALL = "tracker/travel/flight/flight_homepage_all.json"
    }
}