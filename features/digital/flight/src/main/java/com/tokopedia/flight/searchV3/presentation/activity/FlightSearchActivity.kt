package com.tokopedia.flight.searchV3.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.tokopedia.flight.booking.view.activity.FlightBookingActivity
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
import com.tokopedia.flight.search.presentation.activity.FlightSearchReturnActivity
import com.tokopedia.flight.search.presentation.model.FlightAirportViewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.flight.searchV3.presentation.fragment.FlightSearchFragment

class FlightSearchActivity : BaseFlightActivity(), FlightSearchFragment.OnFlightSearchFragmentListener {

    protected lateinit var dateString: String
    protected lateinit var passengerString: String
    protected lateinit var classString: String
    protected lateinit var passDataViewModel: FlightSearchPassDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeDataFromExtras()
        super.onCreate(savedInstanceState)

        setupSearchToolbar()
    }

    override fun getNewFragment(): Fragment = FlightSearchFragment.newInstance(passDataViewModel)

    private fun initializeDataFromExtras() {
        passDataViewModel = intent.extras.getParcelable(EXTRA_PASS_DATA)
        initializeToolbarData()
    }

    protected fun initializeToolbarData() {
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.departureDate
        )
        passengerString = buildPassengerTextFormatted(passDataViewModel.flightPassengerViewModel)
        classString = passDataViewModel.flightClass.title
    }

    protected fun buildPassengerTextFormatted(passData: FlightPassengerViewModel): String {
        var passengerFmt = ""

        if (passData.adult > 0) {
            passengerFmt += "${passData.adult}  ${getString(R.string.flight_dashboard_adult_passenger)}"
            if (passData.children > 0) {
                passengerFmt += ", ${passData.children} ${getString(R.string.flight_dashboard_adult_children)}"
            }
            if (passData.infant > 0) {
                passengerFmt += ", ${passData.infant} ${getString(R.string.flight_dashboard_adult_infant)}"
            }
        }

        return passengerFmt
    }

    protected fun getDepartureAirport(): FlightAirportViewModel = passDataViewModel.departureAirport

    protected fun getArrivalAirport(): FlightAirportViewModel = passDataViewModel.arrivalAirport

    private fun setupSearchToolbar() {
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500))
        val title = "${getDepartureAirport().cityName} ➝ ${getArrivalAirport().cityName}"
        val subtitle = "$dateString | $passengerString | $classString"
        updateTitle(title, subtitle)
    }

    override fun changeDate(flightSearchPassDataViewModel: FlightSearchPassDataViewModel?) {
        passDataViewModel = flightSearchPassDataViewModel!!
        initializeToolbarData()
        setupSearchToolbar()
    }

    override fun selectFlight(selectedFlightID: String, flightPriceViewModel: FlightPriceViewModel, isBestPairing: Boolean) {
        if (passDataViewModel.isOneWay) {
            startActivityForResult(FlightBookingActivity
                    .getCallingIntent(this, passDataViewModel, selectedFlightID, flightPriceViewModel),
                    REQUEST_CODE_BOOKING)
        } else {
            startActivityForResult(FlightSearchReturnActivity
                    .getCallingIntent(this, passDataViewModel, selectedFlightID, isBestPairing, flightPriceViewModel),
                    REQUEST_CODE_RETURN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_RETURN, REQUEST_CODE_BOOKING -> {

            }
        }
    }

    companion object {

        val EXTRA_PASS_DATA = "EXTRA_PASS_DATA"

        private val REQUEST_CODE_BOOKING = 10
        private val REQUEST_CODE_RETURN = 11

        fun getCallingIntent(context: Context, passDataViewModel: FlightSearchPassDataViewModel): Intent {
            val intent = Intent(context, FlightSearchActivity::class.java)
            intent.putExtra(EXTRA_PASS_DATA, passDataViewModel)
            return intent
        }
    }

}
