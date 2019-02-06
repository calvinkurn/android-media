package com.tokopedia.flight.search.presentation.contract;

import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel;

/**
 * @author by furqan on 15/10/18.
 */

public interface FlightSearchReturnContract {

    interface View extends FlightSearchContract.View {

        void showReturnTimeShouldGreaterThanArrivalDeparture();

        void navigateToCart(FlightJourneyViewModel returnFlightSearchViewModel, FlightPriceViewModel flightPriceViewModel);

        void navigateToCart(String selectedFlightReturn, FlightPriceViewModel flightPriceViewModel);

        void showErrorPickJourney();

        void showSeeAllResultView();

        void showSeeBestPairingResultView();

        boolean isOnlyShowBestPair();

        FlightPriceViewModel getPriceViewModel();


    }

    interface Presenter {

        void onFlightSearchSelected(String selectedFlightDeparture, FlightJourneyViewModel returnJourneyModel);

        void onFlightSearchSelected(String selectedFlightDeparture, String selectedFlightReturn);

        void onFlightSearchSelected(String selectedFlightDeparture, FlightJourneyViewModel returnJourneyModel, int adapterPosition);

        void onDestroy();
    }
}
