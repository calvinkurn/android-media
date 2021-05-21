package com.tokopedia.flight.booking.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.booking.presentation.adapter.FlightBookingPassengerAdapter
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.passenger.view.activity.FlightBookingPassengerActivity
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.flight.search.presentation.model.FlightFareModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author by furqan on 15/09/2020
 */
@RunWith(AndroidJUnit4::class)
class FlightBookingActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<FlightBookingActivity> = object : IntentsTestRule<FlightBookingActivity>(FlightBookingActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()
            setupGraphqlMockResponse(FlightBookingMockResponse())
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
                FlightBookingActivity.getCallingIntent(
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
                        "dummy departure id",
                        "dummy departure term",
                        FlightPriceModel(
                                FlightFareModel(
                                        "Rp100.000",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        100000,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0
                                )
                        )
                )
    }

    @Before
    fun setup() {
    }

    @Test
    fun flightBookingAnalytics() {
        Thread.sleep(2000)
        changePassengerData()
        goToPayment()

        Thread.sleep(2000)
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_P1),
                hasAllSuccess())
    }

    private fun changePassengerData() {
        intendingPassengerData()
        Thread.sleep(2000)
        onView(withId(R.id.rv_passengers_info)).perform(RecyclerViewActions
                .actionOnItemAtPosition<FlightBookingPassengerAdapter.ViewHolder>(0, click()))
        Thread.sleep(5000)
    }

    private fun goToPayment() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(2000)
        onView(withId(R.id.nsvFlightBooking)).perform(swipeUp())
        onView(withId(R.id.button_submit)).perform(click())
        Thread.sleep(2000)
        onView(withText("Lanjut Bayar")).perform(click())
        Thread.sleep(5000)
    }

    private fun intendingPassengerData() {
        Intents.intending(IntentMatchers.hasComponent(
                FlightBookingPassengerActivity::class.java.name))
                .respondWith(createPassengerData())
    }

    private fun createPassengerData(): Instrumentation.ActivityResult {
        val passengerData = FlightBookingPassengerModel().apply {
            passengerLocalId = 1
            type = 1
            passengerId = "dummy id"
            passengerTitle = "Mr"
            headerTitle = "Penumpang dewasa"
            passengerFirstName = "Muhammad"
            passengerLastName = "Furqan"
            passengerBirthdate = ""
            flightBookingLuggageMetaViewModels = arrayListOf()
            flightBookingMealMetaViewModels = arrayListOf()
            passengerTitleId = 1
        }
        val resultData = Intent()
        resultData.putExtra(FlightBookingPassengerActivity.EXTRA_PASSENGER, passengerData)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_P1 = "tracker/travel/flight/flight_booking_p1.json"
    }

}
