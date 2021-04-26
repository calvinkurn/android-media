package com.tokopedia.flight.searchV4.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.view.HorizontalProgressBar
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchActivity.Companion.EXTRA_PASS_DATA
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_DEPARTURE_ID
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_BEST_PAIRING
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_COMBINE_DONE
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_PRICE_MODEL
import com.tokopedia.flight.searchV4.presentation.model.*
import com.tokopedia.flight.searchV4.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV4.presentation.viewmodel.FlightSearchReturnViewModel
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.fragment_flight_search_return.*

/**
 * @author by furqan on 15/04/2020
 */
class FlightSearchReturnFragment : FlightSearchFragment() {

    private lateinit var flightSearchReturnViewModel: FlightSearchReturnViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightSearchReturnViewModel.departureJourney.observe(viewLifecycleOwner, Observer {
            if (!flightSearchViewModel.isFilterModelInitialized()) {
                flightSearchViewModel.filterModel = buildFilterModel(FlightFilterModel())
            }
            flightSearchViewModel.filterModel.departureArrivalTime = it.routeList[it.routeList.size - 1].arrivalTimestamp

            renderDepartureJourney(it)
            flightSearchViewModel.fetchSortAndFilter()
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
        showLoading()
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

    override fun onSelectedFromDetail(selectedId: String) {
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

    override fun getFlightSearchTicker(): Ticker = flight_search_ticker

    override fun getSearchHorizontalProgress(): HorizontalProgressBar = horizontal_progress_bar

    override fun buildFilterModel(filterModel: FlightFilterModel): FlightFilterModel {
        filterModel.isBestPairing = flightSearchReturnViewModel.isViewOnlyBestPairing
        filterModel.journeyId = flightSearchReturnViewModel.selectedFlightDepartureId
        filterModel.isReturn = isReturnTrip()
        filterModel.canFilterFreeRapidTest = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_FLIGHT_SHOW_FREE_RAPID_TEST, false)
        filterModel.canFilterSeatDistancing = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_FLIGHT_SHOW_SEAT_DISTANCING, false)

        return filterModel
    }

    override fun renderSearchList(list: List<FlightJourneyModel>) {
        clearAllData()

        if (flightSearchReturnViewModel.isBestPairing &&
                !flightSearchReturnViewModel.isViewOnlyBestPairing &&
                list.isNotEmpty()) {
                    showSeeBestPairingResultView()
        }

        super.renderSearchList(list)

        if(flightSearchReturnViewModel.isViewOnlyBestPairing){
            hidePromoChips()
        }

        if (flightSearchViewModel.isDoneLoadData() && flightSearchReturnViewModel.isViewOnlyBestPairing) {
            showSeeAllResultView()
        }
    }

    override fun onShowAllClicked() {
        super.onShowAllClicked()
        flightSearchReturnViewModel.priceModel.departurePrice?.let {
            showSeeAllResultDialog(it.adult)
        }
    }

    override fun onShowBestPairingClicked() {
        super.onShowBestPairingClicked()
        flightSearchReturnViewModel.priceModel.departurePrice?.let {
            showSeeBestPairingDialog(it.adultCombo)
        }
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
        flightSearchReturnViewModel.priceModel.departurePrice?.let {
            if (flightSearchReturnViewModel.isBestPairing) {
                if (flightSearchReturnViewModel.isViewOnlyBestPairing &&
                        it.adultNumericCombo > 0) {
                    departureTripLabel.setPrice(it.adultCombo)
                } else {
                    departureTripLabel.setPrice(it.adult)
                }
            } else {
                departureTripLabel.setPrice(it.adult)
            }
        }
    }

    private fun showSeeAllResultView() {
        flightSearchReturnViewModel.priceModel.departurePrice?.let {
            adapter.addElement(FlightSearchSeeAllResultModel(FlightCurrencyFormatUtil.convertToIdrPrice(
                    it.adultNumeric, false), false))
            flightSearchReturnViewModel.isViewOnlyBestPairing = true
        }
    }

    private fun showSeeBestPairingResultView() {
        flightSearchReturnViewModel.priceModel.departurePrice?.let {
            adapter.addElement(FlightSearchSeeAllResultModel(FlightCurrencyFormatUtil.convertToIdrPrice(
                    it.adultNumericCombo, false), true))
            flightSearchReturnViewModel.isViewOnlyBestPairing = false
        }
    }

    private fun showSeeAllResultDialog(normalPrice: String) {
        val dialog = DialogUnify(requireActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.flight_search_return_price_change_title_dialog))
        dialog.setDescription(MethodChecker.fromHtml(
                getString(R.string.flight_search_return_price_change_desc_dialog, normalPrice)
        ))
        dialog.setPrimaryCTAText(getString(R.string.flight_search_dialog_proceed_button_text))
        dialog.setPrimaryCTAClickListener {
            flightSearchViewModel.filterModel.isBestPairing = false
            flightSearchReturnViewModel.isViewOnlyBestPairing = false
            clearAllData()
            fetchSortAndFilterData()
            resetDepartureLabelPrice()
            showPromoChips()
            dialog.dismiss()
        }
        dialog.setSecondaryCTAText(getString(R.string.flight_search_return_dialog_abort))
        dialog.setSecondaryCTAClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showSeeBestPairingDialog(bestPairPrice: String) {
        val dialog = DialogUnify(requireActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.flight_search_return_price_change_title_dialog))
        dialog.setDescription(MethodChecker.fromHtml(
                getString(R.string.flight_search_return_price_change_desc_dialog, bestPairPrice)
        ))
        dialog.setPrimaryCTAText(getString(R.string.flight_search_dialog_proceed_button_text))
        dialog.setPrimaryCTAClickListener {
            flightSearchViewModel.filterModel.isBestPairing = true
            flightSearchReturnViewModel.isViewOnlyBestPairing = true
            clearAllData()
            fetchSortAndFilterData()
            resetDepartureLabelPrice()
            dialog.dismiss()
        }
        dialog.setSecondaryCTAText(getString(R.string.flight_search_return_dialog_abort))
        dialog.setSecondaryCTAClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showReturnTimeShouldGreaterThanArrivalDeparture() {
        if (isAdded) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(R.string.flight_search_return_departure_should_greater_message)
            dialog.setPositiveButton(requireActivity().getString(com.tokopedia.abstraction.R.string.title_ok)
            ) { dialog, which ->
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.create().show()
        }
    }

    private fun showErrorPickJourney() {
        view?.let {
            Toaster.build(it, getString(R.string.flight_error_pick_journey), Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR, getString(R.string.flight_booking_action_okay)).show()
        }
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