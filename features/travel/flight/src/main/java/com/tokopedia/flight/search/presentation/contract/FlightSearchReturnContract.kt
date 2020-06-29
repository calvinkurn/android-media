package com.tokopedia.flight.search.presentation.contract

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel

/**
 * @author by furqan on 14/01/19
 */
interface FlightSearchReturnContract {

    interface View: CustomerView {

        fun isOnlyShowBestPair(): Boolean

        fun getFlightPriceViewModel(): FlightPriceModel

        fun showReturnTimeShouldGreaterThanArrivalDeparture()

        fun showErrorPickJourney()

        fun showSeeAllResultView()

        fun showSeeBestPairingResultView()

        fun navigateToCart(returnFlightSearchModel: FlightJourneyModel? = null,
                           selectedFlightReturn: String? = null,
                           flightPriceModel: FlightPriceModel,
                           selectedFlightTerm: String? = null)

    }

    interface Presenter {

        fun onFlightSearchSelected(selectedFlightDeparture: String, returnJourneyModel: FlightJourneyModel, adapterPosition: Int = -1)

        fun onFlightSearchSelected(selectedFlightDeparture: String, selectedFlightReturn: String, selectedTerm: String)

        fun onDestroy()

    }

}