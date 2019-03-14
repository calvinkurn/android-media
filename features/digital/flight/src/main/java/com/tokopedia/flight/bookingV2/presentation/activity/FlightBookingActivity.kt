package com.tokopedia.flight.bookingV2.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flight.FlightModuleRouter
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.bookingV2.presentation.fragment.FlightBookingFragment
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.passenger.domain.FlightPassengerDeleteAllListUseCase
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import rx.Subscriber
import javax.inject.Inject

class FlightBookingActivity : BaseFlightActivity(), HasComponent<FlightBookingComponent> {

    private lateinit var flightPassengerDeleteAllListUseCase: FlightPassengerDeleteAllListUseCase
        @Inject set

    private lateinit var flightBookingFragment: FlightBookingFragment

    override fun getNewFragment(): Fragment {
        val departureId = intent.getStringExtra(EXTRA_FLIGHT_DEPARTURE_ID)
        val arrivalId = intent.getStringExtra(EXTRA_FLIGHT_ARRIVAL_ID)
        val searchPassDataViewModel: FlightSearchPassDataViewModel = intent.getParcelableExtra(EXTRA_PASS_SEARCH_DATA)
        val priceViewModel: FlightPriceViewModel = intent.getParcelableExtra(EXTRA_PRICE)
        flightBookingFragment = FlightBookingFragment.newInstance(searchPassDataViewModel,
                departureId, arrivalId, priceViewModel)
        return flightBookingFragment
    }

    override fun getComponent(): FlightBookingComponent {
        if (application is FlightModuleRouter) {
            return DaggerFlightBookingComponent.builder()
                    .flightComponent(flightComponent)
                    .build()
        }
        throw RuntimeException("Application must implement FlightModuleRouter")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        deleteAllPassengerList(false)
    }

    override fun onBackPressed() {
        deleteAllPassengerList(true)
    }

    override fun getScreenName(): String = FlightAnalytics.Screen.BOOKING

    private fun deleteAllPassengerList(shouldBackPress: Boolean) {
        flightPassengerDeleteAllListUseCase.execute(
                flightPassengerDeleteAllListUseCase.createEmptyRequestParams(),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        if (shouldBackPress) {
                            finish()
                        }
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        if (shouldBackPress) {
                            finish()
                        }
                    }
                }
        )
    }

    companion object {
        private val EXTRA_PASS_SEARCH_DATA = "EXTRA_PASS_SEARCH_DATA"
        private val EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID"
        private val EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID"
        private val EXTRA_PRICE = "EXTRA_PRICE"

        fun getCallingIntent(activity: Activity,
                             passDataViewModel: FlightSearchPassDataViewModel,
                             departureId: String,
                             priceViewModel: FlightPriceViewModel): Intent {
            val intent = Intent(activity, FlightBookingActivity::class.java)
            intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId)
            intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel)
            intent.putExtra(EXTRA_PRICE, priceViewModel)
            return intent
        }

        fun getCallingIntent(activity: Activity,
                             passDataViewModel: FlightSearchPassDataViewModel,
                             departureId: String,
                             returnId: String,
                             priceViewModel: FlightPriceViewModel): Intent {
            val intent = Intent(activity, FlightBookingActivity::class.java)
            intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId)
            intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel)
            intent.putExtra(EXTRA_FLIGHT_ARRIVAL_ID, returnId)
            intent.putExtra(EXTRA_PRICE, priceViewModel)
            return intent
        }
    }
}
