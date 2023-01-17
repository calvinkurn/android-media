package com.tokopedia.flight.homepage.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.flight.R
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 04/08/2020
 */

class FlightHomepageActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    val cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    @get:Rule
    var activityRule = ActivityTestRule(FlightHomepageActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse {
            addMockResponse(
                KEY_CONTAINS_HOMEPAGE_BANNER,
                InstrumentationMockHelper.getRawString(
                    context,
                    com.tokopedia.flight.test.R.raw.response_mock_data_flight_homepage_banner
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_CONTAINS_HOMEPAGE_TRAVEL_VIDEO,
                InstrumentationMockHelper.getRawString(
                    context,
                    com.tokopedia.flight.test.R.raw.response_mock_data_flight_homepage_travel_video
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_CONTAINS_FLIGHT_POPULAR_CITY,
                InstrumentationMockHelper.getRawString(
                    context,
                    com.tokopedia.flight.test.R.raw.response_mock_data_flight_popular_city
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_CONTAINS_FLIGHT_FARE,
                InstrumentationMockHelper.getRawString(
                    context,
                    com.tokopedia.flight.test.R.raw.response_mock_data_flight_calendar_fare
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_CONTAINS_CALENDAR_HOLIDAY,
                InstrumentationMockHelper.getRawString(
                    context,
                    com.tokopedia.flight.test.R.raw.response_mock_data_flight_calendar_holiday
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, FlightHomepageActivity::class.java)
        activityRule.launchActivity(intent)

        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    @Test
    fun validateFlightHomepageP1Tracking() {
        Thread.sleep(3000)

        validateFlightHomepageBannerDisplayedAndScrollable()

        onView(withId(R.id.nsvFlightHomepage)).perform(swipeUp())
        onView(withId(R.id.nsvFlightHomepage)).perform(swipeUp())

        validateFlightHomepageBannerClickableAndPerformClick()
        validateFlightHomepageSearchClick()

        assertThat(cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_P1), hasAllSuccess())
    }

    private fun validateFlightHomepageSearchClick() {
        Thread.sleep(2000)
        onView(withId(R.id.btnFlightSearch)).perform(click())
    }

    private fun validateFlightHomepageBannerDisplayedAndScrollable() {
        Thread.sleep(2000)
        if (getBannerItemCount() > 0) {
            onView(withId(R.id.flightHomepageBanner)).check(matches(isDisplayed()))
            Thread.sleep(1000)
            if (getBannerItemCount() > 1)
                onView(withId(R.id.flightHomepageBanner)).perform(swipeLeft())
            onView(withId(R.id.flightHomepageBanner)).perform(click())
        } else {
            Thread.sleep(1000)
            onView(withId(R.id.flightHomepageBanner)).check(matches(CoreMatchers.not(isDisplayed())))
        }
    }

    private fun validateFlightHomepageBannerClickableAndPerformClick() {
        Thread.sleep(2000)

        if (getBannerItemCount() > 0) {
            onView(withId(R.id.flightHomepageBanner)).perform(swipeRight())
        }
        onView(withId(R.id.flightHomepageBanner)).perform(click())
    }

    private fun getBannerItemCount(): Int {
        val carousel =
            activityRule.activity.findViewById(R.id.flightHomepageBanner) as CarouselUnify
        return carousel.indicatorCount.toIntSafely()
    }

    @Test
    fun validateFlightHomepageAnalyticsP2AndBelow() {
        validateTravelVideoTracking()

        onView(withId(R.id.nsvFlightHomepage)).perform(swipeDown())
        onView(withId(R.id.nsvFlightHomepage)).perform(swipeDown())

        departureAirport()
        arrivalAirport()
        selectTodayDate()
        switchTrip()
        setPassengersCount()
        setPassengersClass()

        Thread.sleep(1000)

        assertThat(cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_ALL), hasAllSuccess())
    }

    private fun departureAirport() {
        Thread.sleep(1000)

        // click on flight departure airport to open bottom sheet to select airport
        onView(withId(R.id.tvFlightOriginAirport)).perform(click())
        Thread.sleep(2000)

        // click on Padang, to set Padang as Departure Airport
        onView(withId(R.id.rvFlightAirport)).check(matches(isDisplayed()))
        onView(withText("Padang, Indonesia")).perform(click())
        Thread.sleep(1000)
    }

    private fun arrivalAirport() {
        Thread.sleep(1000)

        // click on flight arrival airport to open bottom sheet to select airport
        onView(withId(R.id.tvFlightDestinationAirport)).perform(click())
        Thread.sleep(2000)
        // click on Palembang, to set Palembang as Arrival Airport
        onView(withId(R.id.rvFlightAirport)).check(matches(isDisplayed()))
        onView(withText("Palembang, Indonesia")).perform(click())

        Thread.sleep(1000)
    }

    private fun selectTodayDate() {
        Thread.sleep(1000)

        // click departure date
        onView(withId(R.id.tvFlightDepartureDate)).perform(click())
        Thread.sleep(500)

        // select static date - 8
        onView(getElementFromMatchAtPosition(withText("8"), 1)).check(matches(isDisplayed()))
        Thread.sleep(3000)
        onView(getElementFromMatchAtPosition(withText("8"), 1)).perform(click())
        Thread.sleep(3000)
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
        Thread.sleep(1000)

        // set class, Bisnis
        onView(withId(R.id.radioBisnisClass)).perform(click())
        Thread.sleep(1000)
    }

    private fun validateTravelVideoTracking() {
        Thread.sleep(1000)
        onView(withId(R.id.nsvFlightHomepage)).perform(swipeUp())
        onView(withId(R.id.nsvFlightHomepage)).perform(swipeUp())

        Thread.sleep(3000)
        onView(withId(R.id.flightHomepageVideoBanner)).check(matches(isDisplayed()))

        Thread.sleep(3000)
        onView(withId(R.id.flightHomepageVideoBanner)).perform(click())

        Thread.sleep(1000)
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_P1 =
            "tracker/travel/flight/flight_homepage_p1.json"
        private const val ANALYTIC_VALIDATOR_QUERY_ALL =
            "tracker/travel/flight/flight_homepage_all.json"

        private const val KEY_CONTAINS_HOMEPAGE_BANNER = "\"product\": \"FLIGHT\""
        private const val KEY_CONTAINS_HOMEPAGE_TRAVEL_VIDEO = "\"product\": \"FLIGHTPROMOTIONAL\""
        private const val KEY_CONTAINS_FLIGHT_POPULAR_CITY = "flightPopularCity"
        private const val KEY_CONTAINS_FLIGHT_FARE = "flightFare"
        private const val KEY_CONTAINS_CALENDAR_HOLIDAY = "TravelGetHoliday"
    }
}
