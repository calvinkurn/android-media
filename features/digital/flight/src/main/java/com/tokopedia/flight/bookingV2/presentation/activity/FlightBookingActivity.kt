package com.tokopedia.flight.bookingV2.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.flight.bookingV2.di.DaggerFlightBookingComponent
import com.tokopedia.flight.bookingV2.di.FlightBookingComponent
import com.tokopedia.flight.bookingV2.presentation.fragment.FlightBookingFragment
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FlightBookingActivity : BaseFlightActivity(), HasComponent<FlightBookingComponent> {

    lateinit var userSession: UserSessionInterface
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

    override fun getScreenName(): String = FlightAnalytics.Screen.BOOKING

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_LOGIN -> if (userSession.isLoggedIn) recreate() else finish()
        }
    }

    companion object {
        private const val EXTRA_PASS_SEARCH_DATA = "EXTRA_PASS_SEARCH_DATA"
        private const val EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID"
        private const val EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID"
        private const val EXTRA_PRICE = "EXTRA_PRICE"

        private const val REQUEST_CODE_LOGIN = 6

        fun getCallingIntent(activity: Activity,
                             passDataViewModel: FlightSearchPassDataViewModel,
                             departureId: String,
                             priceViewModel: FlightPriceViewModel,
                             returnId: String = ""): Intent {
            val intent = Intent(activity, FlightBookingActivity::class.java)
            intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId)
            intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel)
            intent.putExtra(EXTRA_FLIGHT_ARRIVAL_ID, returnId)
            intent.putExtra(EXTRA_PRICE, priceViewModel)
            return intent
        }
    }
}
