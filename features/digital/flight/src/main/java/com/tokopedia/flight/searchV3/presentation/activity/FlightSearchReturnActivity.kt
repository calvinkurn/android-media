package com.tokopedia.flight.searchV3.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.bookingV2.presentation.activity.FlightBookingActivity
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightFlowUtil
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.flight.searchV3.presentation.fragment.FlightSearchFragment
import com.tokopedia.flight.searchV3.presentation.fragment.FlightSearchReturnFragment

class FlightSearchReturnActivity : FlightSearchActivity(),
        FlightSearchFragment.OnFlightSearchFragmentListener {

    private var selectedDepartureID: String = ""

    override fun getNewFragment(): Fragment {
        val priceViewModel: FlightPriceViewModel = intent.getParcelableExtra<FlightPriceViewModel>(EXTRA_PRICE_VIEW_MODEL)
        return FlightSearchReturnFragment.newInstance(passDataViewModel, selectedDepartureID,
                intent.getBooleanExtra(EXTRA_IS_BEST_PAIRING, false),
                priceViewModel,
                intent.getBooleanExtra(EXTRA_IS_COMBINE_DONE, false))
    }

    override fun initializeToolbarData() {
        selectedDepartureID = intent.getStringExtra(EXTRA_DEPARTURE_ID)

        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.returnDate)
        passengerString = buildPassengerTextFormatted(passDataViewModel.flightPassengerViewModel)
        classString = passDataViewModel.flightClass.title
    }

    override fun getScreenName(): String = FlightAnalytics.Screen.SEARCH_RETURN

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_BOOKING -> {
                if (data != null) {
                    FlightFlowUtil.actionSetResultAndClose(this, intent,
                            data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, 0))
                }
            }
        }
    }

    override fun getDepartureAirport(): FlightAirportViewModel = passDataViewModel.arrivalAirport

    override fun getArrivalAirport(): FlightAirportViewModel = passDataViewModel.departureAirport

    override fun selectFlight(selectedFlightID: String, flightPriceViewModel: FlightPriceViewModel,
                              isBestPairing: Boolean, isCombineDone: Boolean) {
        startActivityForResult(FlightBookingActivity
                .getCallingIntent(this,
                        passDataViewModel,
                        selectedDepartureID,
                        flightPriceViewModel,
                        selectedFlightID),
                REQUEST_CODE_BOOKING)
    }

    companion object {
        val EXTRA_DEPARTURE_ID = "EXTRA_DEPARTURE_ID"
        val EXTRA_IS_BEST_PAIRING = "EXTRA_IS_BEST_PAIRING"
        val EXTRA_PRICE_VIEW_MODEL = "EXTRA_PRICE_VIEW_MODEL"
        val EXTRA_IS_COMBINE_DONE = "EXTRA_IS_COMBINE_DONE"

        private val REQUEST_CODE_BOOKING = 13

        fun getCallingIntent(context: Context,
                             passDataViewModel: FlightSearchPassDataViewModel,
                             selectedDepartureID: String,
                             isBestPairing: Boolean,
                             priceViewModel: FlightPriceViewModel,
                             isCombineDone: Boolean): Intent {
            val intent = Intent(context, FlightSearchReturnActivity::class.java)
            intent.putExtra(EXTRA_PASS_DATA, passDataViewModel)
            intent.putExtra(EXTRA_DEPARTURE_ID, selectedDepartureID)
            intent.putExtra(EXTRA_IS_BEST_PAIRING, isBestPairing)
            intent.putExtra(EXTRA_PRICE_VIEW_MODEL, priceViewModel)
            intent.putExtra(EXTRA_IS_COMBINE_DONE, isCombineDone)

            return intent
        }
    }
}
