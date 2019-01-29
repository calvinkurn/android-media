package com.tokopedia.flight.searchV3.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Dialog
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchActivity.Companion.EXTRA_PASS_DATA
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_DEPARTURE_ID
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_BEST_PAIRING
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_COMBINE_DONE
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_PRICE_VIEW_MODEL
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchReturnContract
import com.tokopedia.flight.searchV3.presentation.presenter.FlightSearchReturnPresenter
import kotlinx.android.synthetic.main.fragment_search_return.*
import javax.inject.Inject

/**
 * @author by furqan on 14/01/19
 */
class FlightSearchReturnFragment : FlightSearchFragment(),
        FlightSearchReturnContract.View {

    lateinit var flightSearchReturnPresenter: FlightSearchReturnPresenter
        @Inject set

    private lateinit var selectedFlightDeparture: String
    var isBestPairing = false
    private var isViewOnlyBestPairing = false

    lateinit var priceViewModel: FlightPriceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val args: Bundle? = savedInstanceState ?: arguments

        if (args != null) {
            selectedFlightDeparture = args.getString(EXTRA_DEPARTURE_ID)
            isBestPairing = args.getBoolean(EXTRA_IS_BEST_PAIRING)
            isViewOnlyBestPairing = args.getBoolean(EXTRA_IS_BEST_PAIRING)
            priceViewModel = args.getParcelable(EXTRA_PRICE_VIEW_MODEL)
            isCombineDone = args.getBoolean(EXTRA_IS_COMBINE_DONE)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        flightSearchReturnPresenter.attachView(this)

        clearAdapterData()

        flightSearchPresenter.getDetailDepartureFlight(selectedFlightDeparture)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(EXTRA_PASS_DATA, flightSearchPassData)
        outState.putString(EXTRA_DEPARTURE_ID, selectedFlightDeparture)
        outState.putBoolean(EXTRA_IS_BEST_PAIRING, isBestPairing)
        outState.putParcelable(EXTRA_PRICE_VIEW_MODEL, priceViewModel)
        outState.putBoolean(EXTRA_IS_COMBINE_DONE, isCombineDone)
    }

    override fun initInjector() {
        super.initInjector()

        if (flightSearchComponent == null) {
            flightSearchComponent = DaggerFlightSearchComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(activity!!.application))
                    .build()
        }

        flightSearchComponent?.inject(this)
    }

    override fun getLayout(): Int = R.layout.fragment_search_return

    override fun getDepartureAirport(): FlightAirportViewModel = flightSearchPassData.arrivalAirport

    override fun getArrivalAirport(): FlightAirportViewModel = flightSearchPassData.departureAirport

    override fun isReturning(): Boolean = true

    override fun onSuccessGetDetailFlightDeparture(flightJourneyViewModel: FlightJourneyViewModel) {
        if (flightJourneyViewModel.airlineDataList != null &&
                flightJourneyViewModel.airlineDataList.size > 1) {
            departure_trip_label.setValueName(String.format(" | %s", getString(R.string.flight_label_multi_maskapai)))
        } else if (flightJourneyViewModel.airlineDataList != null &&
                flightJourneyViewModel.airlineDataList.size == 1) {
            departure_trip_label.setValueName(String.format(" | %s", flightJourneyViewModel.airlineDataList[0].shortName))
        }

        if (flightJourneyViewModel.addDayArrival > 0) {
            departure_trip_label.setValueTime(String.format("%s - %s (+%sh)",
                    flightJourneyViewModel.departureTime,
                    flightJourneyViewModel.arrivalTime,
                    flightJourneyViewModel.addDayArrival.toString()))
        } else {
            departure_trip_label.setValueTime(String.format("%s - %s",
                    flightJourneyViewModel.departureTime,
                    flightJourneyViewModel.arrivalTime))
        }

        departure_trip_label.setValueDestination(String.format("%s - %s",
                flightJourneyViewModel.departureAirport,
                flightJourneyViewModel.arrivalAirport))

        resetDepartureLabelPrice()
    }

    override fun onItemClicked(journeyViewModel: FlightJourneyViewModel?) {
        if (journeyViewModel != null) {
            flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, journeyViewModel)
        }
    }

    override fun onItemClicked(journeyViewModel: FlightJourneyViewModel?, adapterPosition: Int) {
        if (journeyViewModel != null) {
            flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture,
                    journeyViewModel, adapterPosition)
        }
    }

    override fun isOnlyShowBestPair(): Boolean = isViewOnlyBestPairing

    override fun getFlightPriceViewModel(): FlightPriceViewModel = priceViewModel

    override fun showReturnTimeShouldGreaterThanArrivalDeparture() {
        if (isAdded) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(R.string.flight_search_return_departure_should_greater_message)
            dialog.setPositiveButton(activity!!.getString(R.string.title_ok)
            ) { dialog, which ->
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.create().show()
        }
    }

    override fun showErrorPickJourney() {
        NetworkErrorHelper.showRedCloseSnackbar(activity,
                getString(R.string.flight_error_pick_journey))
    }

    override fun showSeeAllResultView() {
        adapter.addElement(FlightSearchSeeAllResultViewModel(priceViewModel.departurePrice.adult))
        isViewOnlyBestPairing = true
    }

    override fun showSeeBestPairingResultView() {
        adapter.addElement(FlightSearchSeeOnlyBestPairingViewModel(
                priceViewModel.departurePrice.adultCombo))
        isViewOnlyBestPairing = false
    }

    override fun navigateToCart(returnFlightSearchViewModel: FlightJourneyViewModel?, selectedFlightReturn: String?, flightPriceViewModel: FlightPriceViewModel) {
        if (returnFlightSearchViewModel != null) {
            onFlightSearchFragmentListener?.selectFlight(returnFlightSearchViewModel.id, flightPriceViewModel, false, true)
        } else if (selectedFlightReturn != null) {
            onFlightSearchFragmentListener?.selectFlight(selectedFlightReturn, flightPriceViewModel, false, true)
        }
    }

    override fun onSelectedFromDetail(selectedId: String) {
        flightSearchReturnPresenter.onFlightSearchSelected(selectedFlightDeparture, selectedId)
    }

    override fun buildFilterModel(flightFilterModel: FlightFilterModel): FlightFilterModel {
        flightFilterModel.isBestPairing = isViewOnlyBestPairing
        flightFilterModel.journeyId = selectedFlightDeparture
        flightFilterModel.isReturn = isReturning()

        return flightFilterModel
    }

    override fun onDestroyView() {
        flightSearchReturnPresenter.onDestroy()
        super.onDestroyView()
    }

    override fun renderSearchList(list: List<FlightJourneyViewModel>, needRefresh: Boolean) {

        if (isBestPairing && !isOnlyShowBestPair()) {
            showSeeBestPairingResultView()
        }

        super.renderSearchList(list, needRefresh)

        if (isDoneLoadData() && isOnlyShowBestPair()) {
            showSeeAllResultView()
        }
    }

    override fun onShowAllClicked() {
        super.onShowAllClicked()

        showSeeAllResultDialog(priceViewModel.departurePrice.adult)
    }

    override fun onShowBestPairingClicked() {
        super.onShowBestPairingClicked()

        showSeeBestPairingDialog(priceViewModel.departurePrice.adultCombo)
    }

    private fun showSeeAllResultDialog(normalPrice: String) {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.flight_search_return_price_change_title_dialog))
        dialog.setDesc(MethodChecker.fromHtml(
                getString(R.string.flight_search_return_price_change_desc_dialog, normalPrice)
        ))
        dialog.setBtnOk(getString(R.string.flight_search_dialog_proceed_button_text))
        dialog.setOnOkClickListener {
            getFilterModel().isBestPairing = false
            isViewOnlyBestPairing = false
            flightSearchPresenter.fetchSortAndFilter(selectedSortOption, getFilterModel(), false)
            resetDepartureLabelPrice()
            dialog.dismiss()
        }
        dialog.setBtnCancel(getString(R.string.flight_search_return_dialog_abort))
        dialog.setOnCancelClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showSeeBestPairingDialog(bestPairPrice: String) {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.flight_search_return_price_change_title_dialog))
        dialog.setDesc(MethodChecker.fromHtml(
                getString(R.string.flight_search_return_price_change_desc_dialog, bestPairPrice)
        ))
        dialog.setBtnOk(getString(R.string.flight_search_dialog_proceed_button_text))
        dialog.setOnOkClickListener {
            getFilterModel().isBestPairing = true
            isViewOnlyBestPairing = true
            flightSearchPresenter.fetchSortAndFilter(selectedSortOption, getFilterModel(), false)
            resetDepartureLabelPrice()
            dialog.dismiss()
        }
        dialog.setBtnCancel(getString(R.string.flight_search_return_dialog_abort))
        dialog.setOnCancelClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun resetDepartureLabelPrice() {
        if (priceViewModel.departurePrice.adultNumericCombo != 0 && isViewOnlyBestPairing) {
            departure_trip_label.setValuePrice(priceViewModel.departurePrice.adultCombo)
        } else {
            departure_trip_label.setValuePrice(priceViewModel.departurePrice.adult)
        }
    }

    companion object {
        fun newInstance(passDataViewModel: FlightSearchPassDataViewModel,
                        selectedDepartureID: String, bestPairing: Boolean,
                        priceViewModel: FlightPriceViewModel,
                        isCombineDone: Boolean): FlightSearchReturnFragment {
            val args = Bundle()
            args.putParcelable(EXTRA_PASS_DATA, passDataViewModel)
            args.putString(EXTRA_DEPARTURE_ID, selectedDepartureID)
            args.putBoolean(EXTRA_IS_BEST_PAIRING, bestPairing)
            args.putParcelable(EXTRA_PRICE_VIEW_MODEL, priceViewModel)
            args.putBoolean(EXTRA_IS_COMBINE_DONE, isCombineDone)

            val fragment = FlightSearchReturnFragment()
            fragment.arguments = args

            return fragment
        }
    }
}