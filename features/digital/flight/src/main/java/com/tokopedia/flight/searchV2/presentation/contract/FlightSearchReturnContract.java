package com.tokopedia.flight.searchV2.presentation.contract;

import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel;

/**
 * @author by furqan on 15/10/18.
 */

public interface FlightSearchReturnContract {

    interface View extends FlightSearchContract.View {

        void showReturnTimeShouldGreaterThanArrivalDeparture();

        void navigateToCart(FlightJourneyViewModel returnFlightSearchViewModel);

        void navigateToCart(String selectedFlightReturn);

        void showErrorPickJourney();

    }

    interface Presenter {

        void onFlightSearchSelected(String selectedFlightDeparture, FlightJourneyViewModel journeyViewModel);

        void onFlightSearchSelected(String selectedFlightDeparture, String selectedFlightReturn);

        void onFlightSearchSelected(String selectedFlightDeparture, FlightJourneyViewModel journeyViewModel, int adapterPosition);

        void onDestroy();
    }
}
