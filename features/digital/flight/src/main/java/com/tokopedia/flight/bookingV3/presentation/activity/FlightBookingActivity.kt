package com.tokopedia.flight.bookingV3.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.bookingV3.presentation.fragment.FlightBookingFragment
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by jessica on 2019-10-23
 */

class FlightBookingActivity: BaseFlightActivity(), HasComponent<FlightBookingComponent> {

    lateinit var userSession: UserSessionInterface
        @Inject set

    override fun getNewFragment(): Fragment {
        val departureId = intent.getStringExtra(EXTRA_FLIGHT_DEPARTURE_ID)
        val arrivalId = intent.getStringExtra(EXTRA_FLIGHT_ARRIVAL_ID)
        val departureTerm = intent.getStringExtra(EXTRA_FLIGHT_DEPARTURE_TERM)
        val returnTerm = intent.getStringExtra(EXTRA_FLIGHT_ARRIVAL_TERM)
        val searchPassDataViewModel: FlightSearchPassDataViewModel = intent.getParcelableExtra(EXTRA_PASS_SEARCH_DATA)
        val priceViewModel: FlightPriceViewModel = intent.getParcelableExtra(EXTRA_PRICE)
        return FlightBookingFragment.newInstance(searchPassDataViewModel,
                departureId, arrivalId, departureTerm, returnTerm, priceViewModel)
    }

    override fun getComponent(): FlightBookingComponent {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(flightComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        if (!userSession.isLoggedIn) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_LOGIN -> if (userSession.isLoggedIn) recreate() else finish()
        }
    }

    companion object {
        private const val REQUEST_CODE_LOGIN = 6
        private const val EXTRA_PASS_SEARCH_DATA = "EXTRA_PASS_SEARCH_DATA"
        private const val EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID"
        private const val EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID"
        private const val EXTRA_FLIGHT_DEPARTURE_TERM = "EXTRA_FLIGHT_DEPARTURE_TERM"
        private const val EXTRA_FLIGHT_ARRIVAL_TERM = "EXTRA_FLIGHT_ARRIVAL_TERM"
        private const val EXTRA_PRICE = "EXTRA_PRICE"

        fun getCallingIntent(activity: Activity,
                             passDataViewModel: FlightSearchPassDataViewModel,
                             departureId: String,
                             departureTerm: String,
                             priceViewModel: FlightPriceViewModel,
                             returnId: String = "",
                             returnTerm: String = ""): Intent {
            val intent = Intent(activity, FlightBookingActivity::class.java)
            intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId)
            intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel)
            intent.putExtra(EXTRA_FLIGHT_ARRIVAL_ID, returnId)
            intent.putExtra(EXTRA_FLIGHT_DEPARTURE_TERM, departureTerm)
            intent.putExtra(EXTRA_FLIGHT_ARRIVAL_TERM, returnTerm)
            intent.putExtra(EXTRA_PRICE, priceViewModel)
            return intent
        }
    }
}