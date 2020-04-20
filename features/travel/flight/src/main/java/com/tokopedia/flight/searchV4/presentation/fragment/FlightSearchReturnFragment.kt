package com.tokopedia.flight.searchV4.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchActivity.Companion.EXTRA_PASS_DATA
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_DEPARTURE_ID
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_BEST_PAIRING
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_COMBINE_DONE
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_PRICE_MODEL
import com.tokopedia.flight.searchV4.presentation.model.SearchErrorEnum
import com.tokopedia.flight.searchV4.presentation.viewmodel.FlightSearchReturnViewModel
import kotlinx.android.synthetic.main.fragment_flight_search_return.*

/**
 * @author by furqan on 15/04/2020
 */
class FlightSearchReturnFragment : FlightSearchFragment() {

    private lateinit var flightSearchReturnViewModel: FlightSearchReturnViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightSearchReturnViewModel.departureJourney.observe(viewLifecycleOwner, Observer {
            renderDepartureJourney(it)
        })

        flightSearchReturnViewModel.searchErrorStringId.observe(viewLifecycleOwner, Observer {
            when (it) {
                SearchErrorEnum.NO_ERRORS -> navigateToCart()
                SearchErrorEnum.ERROR_PICK_JOURNEY -> showErrorPickJourney()
                SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME -> showReturnTimeShouldGreaterThanArrivalDeparture()
                else -> {
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.clearAllElements()
    }

    override fun getLayout(): Int = R.layout.fragment_flight_search_return

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun isReturnTrip(): Boolean = true

    override fun onItemClicked(journeyModel: FlightJourneyModel?) {
        flightSearchReturnViewModel.onFlightSearchSelected(flightSearchViewModel.flightSearchPassData, journeyModel)
    }

    override fun onItemClicked(journeyModel: FlightJourneyModel?, adapterPosition: Int) {
        flightSearchReturnViewModel.onFlightSearchSelected(flightSearchViewModel.flightSearchPassData, journeyModel, adapterPosition)
    }

    override fun onSelectedFromDetail(selectedId: String, selectedTerm: String) {
        flightSearchReturnViewModel.onFlightSearchSelectFromDetail(flightSearchViewModel.flightSearchPassData, selectedId)
    }

    override fun initViewModels() {
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightSearchReturnViewModel = viewModelProvider.get(FlightSearchReturnViewModel::class.java)

            arguments?.let { args ->
                flightSearchReturnViewModel.selectedFlightDepartureId = args.getString(EXTRA_DEPARTURE_ID, "")
                flightSearchReturnViewModel.isViewOnlyBestPairing = args.getBoolean(EXTRA_IS_BEST_PAIRING, false)
                flightSearchReturnViewModel.isBestPairing = args.getBoolean(EXTRA_IS_BEST_PAIRING, false)
                args.getParcelable<FlightPriceModel>(EXTRA_PRICE_MODEL)?.let {
                    flightSearchReturnViewModel.priceModel = it
                }
            }

            flightSearchReturnViewModel.getDepartureJourneyDetail()
        }
        super.initViewModels()
    }

    override fun getDepartureAirport(): FlightAirportModel = flightSearchViewModel.flightSearchPassData.arrivalAirport

    override fun getArrivalAirport(): FlightAirportModel = flightSearchViewModel.flightSearchPassData.departureAirport

    override fun buildFilterModel(filterModel: FlightFilterModel): FlightFilterModel {
        filterModel.isBestPairing = flightSearchReturnViewModel.isViewOnlyBestPairing
        filterModel.journeyId = flightSearchReturnViewModel.selectedFlightDepartureId
        filterModel.isReturn = isReturnTrip()

        return filterModel
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

    private fun showReturnTimeShouldGreaterThanArrivalDeparture() {
        if (isAdded) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(R.string.flight_search_return_departure_should_greater_message)
            dialog.setPositiveButton(activity!!.getString(com.tokopedia.abstraction.R.string.title_ok)
            ) { dialog, which ->
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.create().show()
        }
    }

    private fun showErrorPickJourney() {
        NetworkErrorHelper.showRedCloseSnackbar(activity,
                getString(R.string.flight_error_pick_journey))
    }

    private fun navigateToCart() {
        flightSearchReturnViewModel.selectedReturnJourney?.let {
            onFlightSearchFragmentListener?.selectFlight(it.id, it.term,
                    flightSearchReturnViewModel.priceModel,
                    isBestPairing = false, isCombineDone = true,
                    requestId = flightSearchViewModel.flightSearchPassData.searchRequestId ?: "")
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