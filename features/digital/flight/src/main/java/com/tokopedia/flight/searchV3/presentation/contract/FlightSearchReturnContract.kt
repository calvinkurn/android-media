package com.tokopedia.flight.searchV3.presentation.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel

/**
 * @author by furqan on 14/01/19
 */
interface FlightSearchReturnContract {

    interface View: CustomerView {

        fun isOnlyShowBestPair(): Boolean

        fun getFlightPriceViewModel(): FlightPriceViewModel

        fun showReturnTimeShouldGreaterThanArrivalDeparture()

        fun showErrorPickJourney()

        fun showSeeAllResultView()

        fun showSeeBestPairingResultView()

        fun navigateToCart(returnFlightSearchViewModel: FlightJourneyViewModel? = null,
                           selectedFlightReturn: String? = null,
                           flightPriceViewModel: FlightPriceViewModel)

    }

    interface Presenter {

        fun onFlightSearchSelected(selectedFlightDeparture: String, returnJourneyViewModel: FlightJourneyViewModel, adapterPosition: Int = -1)

        fun onFlightSearchSelected(selectedFlightDeparture: String, selectedFlightReturn: String)

        fun onDestroy()

    }

}