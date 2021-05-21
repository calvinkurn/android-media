package com.tokopedia.flight.search.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.bookingV3.presentation.activity.FlightBookingActivity
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightFlowUtil
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment
import com.tokopedia.flight.search.presentation.fragment.FlightSearchReturnFragment
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel

/**
 * @author by furqan on 15/04/2020
 */
class FlightSearchReturnActivity : FlightSearchActivity(),
        FlightSearchFragment.OnFlightSearchFragmentListener {

    private var selectedDepartureId: String = ""
    private var selectedDepartureTerm: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        selectedDepartureId = intent.getStringExtra(EXTRA_DEPARTURE_ID)
        selectedDepartureTerm = intent.getStringExtra(EXTRA_DEPARTURE_TERM)

        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        val priceModel: FlightPriceModel = intent.getParcelableExtra(EXTRA_PRICE_MODEL)
        return FlightSearchReturnFragment.newInstance(flightSearchPassDataModel,
                selectedDepartureId,
                intent.getBooleanExtra(EXTRA_IS_BEST_PAIRING, false),
                priceModel,
                intent.getBooleanExtra(EXTRA_IS_COMBINE_DONE, true))
    }

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

    override fun getScreenName(): String = FlightAnalytics.Screen.SEARCH_RETURN

    override fun getDepartureAirport(): FlightAirportModel = flightSearchPassDataModel.arrivalAirport

    override fun getArrivalAirport(): FlightAirportModel = flightSearchPassDataModel.departureAirport

    override fun selectFlight(selectedFlightID: String, selectedTerm: String, flightPriceModel: FlightPriceModel, isBestPairing: Boolean, isCombineDone: Boolean, requestId: String) {
        flightSearchPassDataModel.searchRequestId = requestId
        startActivityForResult(FlightBookingActivity.getCallingIntent(this,
                flightSearchPassDataModel, selectedDepartureId, selectedDepartureTerm,
                flightPriceModel, selectedFlightID, selectedTerm), REQUEST_CODE_BOOKING)
    }

    override fun isReturnPage(): Boolean = true

    override fun initializeToolbarData() {
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.FORMAT_DATE_SHORT_MONTH,
                flightSearchPassDataModel.returnDate)
        passengerString = buildPassengerTextFormatted(flightSearchPassDataModel.flightPassengerModel)
        classString = flightSearchPassDataModel.flightClass.title
    }

    companion object {
        const val EXTRA_DEPARTURE_ID = "EXTRA_DEPARTURE_ID"
        const val EXTRA_DEPARTURE_TERM = "EXTRA_DEPARTURE_TERM"
        const val EXTRA_IS_BEST_PAIRING = "EXTRA_IS_BEST_PAIRING"
        const val EXTRA_PRICE_MODEL = "EXTRA_PRICE_MODEL"
        const val EXTRA_IS_COMBINE_DONE = "EXTRA_IS_COMBINE_DONE"

        private const val REQUEST_CODE_BOOKING = 13

        fun getCallingIntent(context: Context,
                             passDataModel: FlightSearchPassDataModel,
                             selectedDepartureId: String,
                             selectedDepartureTerm: String,
                             isBestPairing: Boolean,
                             priceModel: FlightPriceModel,
                             isCombineDone: Boolean): Intent =
                Intent(context, FlightSearchReturnActivity::class.java)
                        .putExtra(EXTRA_PASS_DATA, passDataModel)
                        .putExtra(EXTRA_DEPARTURE_ID, selectedDepartureId)
                        .putExtra(EXTRA_DEPARTURE_TERM, selectedDepartureTerm)
                        .putExtra(EXTRA_IS_BEST_PAIRING, isBestPairing)
                        .putExtra(EXTRA_PRICE_MODEL, priceModel)
                        .putExtra(EXTRA_IS_COMBINE_DONE, isCombineDone)
    }
}