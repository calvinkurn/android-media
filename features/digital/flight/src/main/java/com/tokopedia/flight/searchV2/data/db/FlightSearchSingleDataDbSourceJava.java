package com.tokopedia.flight.searchV2.data.db;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Rizky on 01/10/18.
 */
public class FlightSearchSingleDataDbSourceJava {
    private FlightJourneyDao flightJourneyDao;
    private FlightRouteDao flightRouteDao;

    @Inject
    public FlightSearchSingleDataDbSourceJava(FlightJourneyDao flightJourneyDao,
                                              FlightRouteDao flightRouteDao) {
        this.flightJourneyDao = flightJourneyDao;
        this.flightRouteDao = flightRouteDao;
    }

    public Observable<List<FlightJourneyTable>> getSearchSingle() {
        List<FlightJourneyTable> journeys = new ArrayList<>();
        return Observable.just(journeys);
    }

    public void insert(List<FlightJourneyTable> items) {

    }
}
