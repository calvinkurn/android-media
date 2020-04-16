package com.tokopedia.flight.searchV4.presentation.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchActivity.Companion.EXTRA_PASS_DATA
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_DEPARTURE_ID
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_BEST_PAIRING
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_COMBINE_DONE
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_PRICE_MODEL
import com.tokopedia.flight.searchV4.presentation.viewmodel.FlightSearchReturnViewModel
import kotlinx.android.synthetic.main.fragment_flight_search_return.*

/**
 * @author by furqan on 15/04/2020
 */
class FlightSearchReturnFragment : FlightSearchFragment() {

    private lateinit var flightSearchReturnViewModel: FlightSearchReturnViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightSearchReturnViewModel = viewModelProvider.get(FlightSearchReturnViewModel::class.java)

            arguments?.let {
                flightSearchReturnViewModel.setSelectedDepartureId(it.getString(EXTRA_DEPARTURE_ID, ""))
                flightSearchReturnViewModel.isViewOnlyBestPairing = it.getBoolean(EXTRA_IS_BEST_PAIRING, false)
                flightSearchReturnViewModel.isBestPairing = it.getBoolean(EXTRA_IS_BEST_PAIRING, false)
                flightSearchReturnViewModel.priceModel = it.getParcelable(EXTRA_PRICE_MODEL)
                flightSearchViewModel.isCombineDone = it.getBoolean(EXTRA_IS_COMBINE_DONE, true)
            }

            flightSearchReturnViewModel.getDepartureJourneyDetail()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightSearchReturnViewModel.departureJourney.observe(viewLifecycleOwner, Observer {
            renderDepartureJourney(it)
        })
    }

    override fun getLayout(): Int = R.layout.fragment_flight_search_return

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun isReturnTrip(): Boolean = true

    override fun onItemClicked(journeyModel: FlightJourneyModel?) {
        super.onItemClicked(journeyModel)
    }

    override fun onItemClicked(journeyViewModel: FlightJourneyModel?, adapterPosition: Int) {
        super.onItemClicked(journeyViewModel, adapterPosition)
    }

    private fun renderDepartureJourney(flightJourneyModel: FlightJourneyModel) {
        if (flightJourneyModel.airlineDataList != null &&
                flightJourneyModel.airlineDataList.size > 1) {
            departureTripLabel.setAirline(getString(R.string.flight_label_multi_maskapai))
        } else if (flightJourneyModel.airlineDataList != null &&
                flightJourneyModel.airlineDataList.size == 1) {
            departureTripLabel.setAirline(flightJourneyModel.airlineDataList[0].shortName)
        }

        departureTripLabel.setDate("${FlightDateUtil.formatToUi(flightSearchViewModel.flightSearchPassData.departureDate)} | ")

        if (flightJourneyModel.addDayArrival > 0) {
            departureTripLabel.setTime("${flightJourneyModel.departureTime} - ${flightJourneyModel.arrivalTime} (+${flightJourneyModel.addDayArrival}h)")
        } else {
            departureTripLabel.setTime("${flightJourneyModel.departureTime} - ${flightJourneyModel.arrivalTime}")
        }

        departureTripLabel.setDestination("${flightJourneyModel.departureAirport} - ${flightJourneyModel.arrivalAirport} | ")

        resetDepartureLabelPrice()
    }

    private fun resetDepartureLabelPrice() {
        if (flightSearchReturnViewModel.isBestPairing) {
            if (flightSearchReturnViewModel.isViewOnlyBestPairing &&
                    flightSearchReturnViewModel.priceModel.departurePrice.adultNumericCombo > 0) {
                departureTripLabel.setPrice(flightSearchReturnViewModel.priceModel.departurePrice.adultCombo)
            } else {
                departureTripLabel.setPrice(flightSearchReturnViewModel.priceModel.departurePrice.adult)
            }
        } else {
            departureTripLabel.setPrice(flightSearchReturnViewModel.priceModel.departurePrice.adult)
        }
    }

    companion object {
        fun newInstance(passDataModel: FlightSearchPassDataModel,
                        selectedDepartureId: String,
                        isBestPairing: Boolean,
                        priceModel: FlightPriceModel,
                        isCombineDone: Boolean): FlightSearchReturnFragment =
                FlightSearchReturnFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_PASS_DATA, passDataModel)
                        putString(EXTRA_DEPARTURE_ID, selectedDepartureId)
                        putBoolean(EXTRA_IS_BEST_PAIRING, isBestPairing)
                        putParcelable(EXTRA_PRICE_MODEL, priceModel)
                        putBoolean(EXTRA_IS_COMBINE_DONE, isCombineDone)
                    }
                }
    }
}