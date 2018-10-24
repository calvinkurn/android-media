package com.tokopedia.flight.search.view.fragment.flightinterface;

import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;

/**
 * Created by User on 11/2/2017.
 */

public interface OnFlightFilterListener {
    FlightSearchStatisticModel getFlightSearchStatisticModel();

    FlightFilterModel getFlightFilterModel();

    void onFilterModelChanged(FlightFilterModel flightFilterModel);
}
