package com.tokopedia.flight.search.view;

import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightSearchReturnView extends FlightSearchView {

    void showReturnTimeShouldGreaterThanArrivalDeparture();

    void navigateToCart(FlightSearchViewModel returnFlightSearchViewModel);

    void navigateToCart(String selectedFlightReturn);

    void showErrorPickJourney();
}
