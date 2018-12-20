package com.tokopedia.flight.cancellation.domain.mapper;

import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationViewModelMapper {

    @Inject
    public FlightCancellationViewModelMapper() {
    }

    public List<FlightCancellationViewModel> transform(List<Passenger> passengerList) {
        HashMap<String, List<FlightCancellationPassengerViewModel>> map = new HashMap<>();

        for (Passenger passenger : passengerList) {
            if (map.containsKey(passenger.getJourneyId())) {
                map.get(passenger.getJourneyId()).add(transform(passenger));
            } else {
                List<FlightCancellationPassengerViewModel> passengerViewModelList = new ArrayList<>();
                passengerViewModelList.add(transform(passenger));
                map.put(passenger.getJourneyId(), passengerViewModelList);
            }
        }

        List<FlightCancellationViewModel> flightCancellationViewModelList = new ArrayList<>();

        for (Map.Entry<String, List<FlightCancellationPassengerViewModel>> entry : map.entrySet()) {
            FlightCancellationViewModel flightCancellationViewModel = new FlightCancellationViewModel();

            FlightCancellationJourney flightCancellationJourney = new FlightCancellationJourney();
            flightCancellationJourney.setJourneyId(entry.getKey());

            flightCancellationViewModel.setFlightCancellationJourney(flightCancellationJourney);
            flightCancellationViewModel.setPassengerViewModelList(entry.getValue());

            flightCancellationViewModelList.add(flightCancellationViewModel);
        }

        return flightCancellationViewModelList;
    }

    public FlightCancellationPassengerViewModel transform(Passenger passenger) {

        FlightCancellationPassengerViewModel flightCancellationPassengerViewModel = new FlightCancellationPassengerViewModel();
        flightCancellationPassengerViewModel.setPassengerId(passenger.getPassengerId());
        flightCancellationPassengerViewModel.setTitle(passenger.getTitle());
        flightCancellationPassengerViewModel.setType(passenger.getType());
        flightCancellationPassengerViewModel.setFirstName(passenger.getFirstName());
        flightCancellationPassengerViewModel.setLastName(passenger.getLastName());
        flightCancellationPassengerViewModel.setRelationId(passenger.getRelationId());
        flightCancellationPassengerViewModel.setRelations(passenger.getRelations());

        return flightCancellationPassengerViewModel;
    }
}
