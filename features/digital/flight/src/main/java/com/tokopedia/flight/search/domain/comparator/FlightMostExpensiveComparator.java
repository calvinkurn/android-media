package com.tokopedia.flight.search.domain.comparator;

import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Comparator;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightMostExpensiveComparator implements Comparator<FlightSearchViewModel> {
    @Override
    public int compare(FlightSearchViewModel o1, FlightSearchViewModel o2) {
        return o2.getFare().getAdultNumeric() - o1.getFare().getAdultNumeric();
    }
}
