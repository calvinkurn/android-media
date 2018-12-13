package com.tokopedia.flight.search.presentation.fragment;

import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel;

/**
 * Created by User on 11/2/2017.
 */

public interface OnFlightFilterListener {
    FlightSearchStatisticModel getFlightSearchStatisticModel();

    FlightFilterModel getFlightFilterModel();

    void onFilterModelChanged(FlightFilterModel flightFilterModel);
}
