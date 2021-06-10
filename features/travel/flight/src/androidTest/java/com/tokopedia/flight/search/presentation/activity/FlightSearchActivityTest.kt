package com.tokopedia.flight.search.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.promo_chips.presentation.adapter.viewholder.FlightPromoChipsViewHolder
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightSearchViewHolder
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by furqan on 25/08/2020
 */
@RunWith(AndroidJUnit4::class)
class FlightSearchActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<FlightSearchActivity> = object : IntentsTestRule<FlightSearchActivity>(FlightSearchActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(FlightSearchMockResponse())
            gtmLogDBSource.deleteAll().subscribe()
        }

        override fun getActivityIntent(): Intent =
                FlightSearchActivity.getCallingIntent(
                        context,
                        FlightSearchPassDataModel(
                                "2021-11-11",
                                "",
                                true,
                                FlightPassengerModel(1, 0, 0),
                                FlightAirportModel().apply {
                                    cityCode = "JKTA"
                                    cityId = ""
                                    cityName = "Jakarta"
                                    cityAirports = arrayListOf("CGK", "HLP")
                                    countryName = "Indonesia"
                                    airportName = ""
                                    airportCode = "CGK"
                                },
                                FlightAirportModel().apply {
                                    cityCode = ""
                                    cityId = ""
                                    cityName = "Banda Aceh"
                                    cityAirports = arrayListOf()
                                    countryName = "Indonesia"
                                    airportName = "Bandara Intl. Sultan Iskandar Muda"
                                    airportCode = "BTJ"
                                },
                                FlightClassModel(1, "Ekonomi"),
                                "", ""
                        ),
                        false
                )
    }

    @Before
    fun setup() {
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validateFlightSearchPageP2AndBelowTracking() {
        Thread.sleep(2000)
        quickFilter()
        changeSearch()

        Thread.sleep(2000)
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_ALL),
                hasAllSuccess())
    }

    private fun quickFilter() {
        Thread.sleep(2000)
        onView(AllOf.allOf(withId(R.id.chip_text), withText("Langsung"))).perform(click())
        Thread.sleep(1000)
    }

    private fun changeSearch() {
        onView(withTagValue(CoreMatchers.equalTo("TagChangeSearchButton"))).perform(click())
        setPassengersCount()

        onView(withId(R.id.btnFlightSearch)).perform(click())
    }

    private fun setPassengersCount() {
        Thread.sleep(1000)

        // click on flight passengers to open bottom sheet to set passengers number
        onView(withId(R.id.tvFlightPassenger)).perform(click())

        // change passengers
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 0)).perform(click())
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 0)).perform(click())

        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 1)).perform(click())
        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 1)).perform(click())

        onView(CommonMatcher.getElementFromMatchAtPosition(withId(R.id.quantity_editor_add), 2)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.btnFlightPassenger)).perform(click())
        Thread.sleep(1000)
    }

    @Test
    fun validateFlightSearchPageP1Tracking() {
        Thread.sleep(3000)

        try {
            onView(withId(R.id.text_next)).check(matches(isDisplayed())).perform(click())
        } catch (e: NoMatchingViewException) { }

        assert(getJourneyItemCount() > 1)

        if (getJourneyItemCount() > 0) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<FlightSearchViewHolder>(0, click()))
        }

        Thread.sleep(2000)
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_P1),
                hasAllSuccess())
    }

    @Test
    fun promoChipsOnClicked(){
        Thread.sleep(5000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view_promo_chips), withParent(withId(R.id.widget_flight_promo_chips)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions
                .actionOnItemAtPosition<FlightPromoChipsViewHolder>(0, click()))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_PROMO_CHIPS),
                hasAllSuccess())
    }

    private fun getJourneyItemCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_P1 = "tracker/travel/flight/flight_search_p1.json"
        private const val ANALYTIC_VALIDATOR_QUERY_ALL = "tracker/travel/flight/flight_search_all.json"
        private const val ANALYTIC_VALIDATOR_PROMO_CHIPS = "tracker/travel/flight/flight_srp_promochips.json"
    }
}