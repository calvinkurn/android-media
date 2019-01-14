package com.tokopedia.flight.searchV3.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel
import com.tokopedia.flight.search.di.DaggerFlightSearchComponent
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchActivity.Companion.EXTRA_PASS_DATA
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_DEPARTURE_ID
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_BEST_PAIRING
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_IS_COMBINE_DONE
import com.tokopedia.flight.searchV3.presentation.activity.FlightSearchReturnActivity.Companion.EXTRA_PRICE_VIEW_MODEL
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchReturnContract
import com.tokopedia.flight.searchV3.presentation.presenter.FlightSearchReturnPresenter
import javax.inject.Inject

/**
 * @author by furqan on 14/01/19
 */
class FlightSearchReturnFragment : FlightSearchFragment(),
        FlightSearchReturnContract.View {

    lateinit var flightSearchReturnPresenter: FlightSearchReturnPresenter
        @Inject set

    lateinit var selectedFlightDeparture: String
    var isBestPairing = false
    var isViewOnlyBestPairing = false
    var isCombineDone = false

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
                    .flightComponent(FlightComponentInstance.getFlightComponent(activity.application))
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