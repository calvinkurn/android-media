package com.tokopedia.flight.search.presentation.model;

import java.util.List;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightSearchViewModel {

    private List<FlightJourneyViewModel> flightJourneyViewModels;
    private FlightSearchMetaViewModel flightSearchMetaViewModel;

    public FlightSearchViewModel(List<FlightJourneyViewModel> flightJourneyViewModels, FlightSearchMetaViewModel flightSearchMetaViewModel) {
        this.flightJourneyViewModels = flightJourneyViewModels;
        this.flightSearchMetaViewModel = flightSearchMetaViewModel;
    }

    public List<FlightJourneyViewModel> getFlightJourneyViewModels() {
        return flightJourneyViewModels;
    }

    public FlightSearchMetaViewModel getFlightSearchMetaViewModel() {
        return flightSearchMetaViewModel;
    }

}
