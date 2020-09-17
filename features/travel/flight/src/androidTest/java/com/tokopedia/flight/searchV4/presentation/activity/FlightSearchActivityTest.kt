package com.tokopedia.flight.searchV4.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.searchV4.presentation.adapter.viewholder.FlightSearchViewHolder
import com.tokopedia.flight.searchV4.presentation.model.FlightSearchPassDataModel
import com.tokopedia.test.application.util.setupGraphqlMockResponse
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
        }
        override fun getActivityIntent(): Intent =
                FlightSearchActivity.getCallingIntent(
                        context,
                        FlightSearchPassDataModel(
                                "2020-11-11",
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
                                    airportCode = ""
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
        gtmLogDBSource.deleteAll().subscribe()
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validateFlightSearchPageP1Tracking() {
        Thread.sleep(2000)
        assert(getJourneyItemCount() > 1)

        Thread.sleep(3000)
        if (getJourneyItemCount() > 0) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<FlightSearchViewHolder>(0, click()))
        }

        Thread.sleep(3000)
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_P1),
                hasAllSuccess())
    }

    private fun getJourneyItemCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_P1 = "tracker/travel/flight/flight_search_p1.json"
    }
}