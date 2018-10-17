package com.tokopedia.flight.searchV2.presentation.fragment;

import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;

/**
 * Created by Rizky on 17/10/18.
 */
public interface OnFlightSearchV2FilterListener {

    FlightSearchStatisticModel getFlightSearchStatisticModel();

    FlightFilterModel getFlightFilterModel();

    void onFilterModelChanged(FlightFilterModel flightFilterModel);

}
