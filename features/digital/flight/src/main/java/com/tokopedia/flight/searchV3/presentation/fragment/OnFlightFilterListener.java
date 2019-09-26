package com.tokopedia.flight.searchV3.presentation.fragment;

import com.tokopedia.flight.searchV3.presentation.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.flight.searchV3.presentation.model.filter.FlightFilterModel;

/**
 * Created by User on 11/2/2017.
 */

public interface OnFlightFilterListener {
    FlightSearchStatisticModel getFlightSearchStatisticModel();

    FlightFilterModel getFlightFilterModel();

    void onFilterModelChanged(FlightFilterModel flightFilterModel);
}
