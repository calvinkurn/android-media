package com.tokopedia.flight.searchV4.presentation.activity

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
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
        setupGraphqlMockResponse(FlightSearchMockResponse())
//        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validateFlightSearchPageP1Tracking() {
        Thread.sleep(2000)
        assert(getJourneyItemCount() > 1)
        Log.d("JUMLAH", getJourneyItemCount().toString())

        Thread.sleep(2000)

//        if (getJourneyItemCount() > 0) {
//            onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions
//                    .actionOnItemAtPosition<FlightSearchViewHolder>(0, ViewActions.click()))
//        }
    }

    private fun getJourneyItemCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }
}