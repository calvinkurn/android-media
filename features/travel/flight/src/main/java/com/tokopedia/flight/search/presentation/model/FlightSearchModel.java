package com.tokopedia.flight.search.presentation.model;

import java.util.List;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightSearchModel {

    private List<FlightJourneyModel> flightJourneyViewModels;
    private FlightSearchMetaModel flightSearchMetaViewModel;

    public FlightSearchModel(List<FlightJourneyModel> flightJourneyViewModels, FlightSearchMetaModel flightSearchMetaViewModel) {
        this.flightJourneyViewModels = flightJourneyViewModels;
        this.flightSearchMetaViewModel = flightSearchMetaViewModel;
    }

    public List<FlightJourneyModel> getFlightJourneyViewModels() {
        return flightJourneyViewModels;
    }

    public FlightSearchMetaModel getFlightSearchMetaViewModel() {
        return flightSearchMetaViewModel;
    }

}
