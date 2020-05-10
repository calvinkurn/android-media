package com.tokopedia.flight.cancellation.domain.mapper;

import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationViewModelMapper {
    private static final String CANCELLABLES_KEY = "cancellablePassengers";
    private static final String NON_CANCELLABLES_KEY = "nonCancellablePassengers";

    @Inject
    public FlightCancellationViewModelMapper() {
    }

    public List<FlightCancellationModel> transform(Map<String, List<Passenger>> passengerMap) {
        HashMap<String, List<FlightCancellationPassengerModel>> map = new HashMap<>();

        if (passengerMap.containsKey(CANCELLABLES_KEY)) {
            for (Passenger passenger : passengerMap.get(CANCELLABLES_KEY)) {
                if (map.containsKey(passenger.getJourneyId())) {
                    map.get(passenger.getJourneyId()).add(transform(passenger));
                } else {
                    List<FlightCancellationPassengerModel> passengerViewModelList = new ArrayList<>();
                    passengerViewModelList.add(transform(passenger));
                    map.put(passenger.getJourneyId(), passengerViewModelList);
                }
            }
        }

        if (passengerMap.containsKey(NON_CANCELLABLES_KEY)) {
            for (Passenger passenger : passengerMap.get(NON_CANCELLABLES_KEY)) {
                if (map.containsKey(passenger.getJourneyId())) {
                    map.get(passenger.getJourneyId()).add(transform(passenger));
                } else {
                    List<FlightCancellationPassengerModel> passengerViewModelList = new ArrayList<>();
                    passengerViewModelList.add(transform(passenger));
                    map.put(passenger.getJourneyId(), passengerViewModelList);
                }
            }
        }

        List<FlightCancellationModel> flightCancellationViewModelList = new ArrayList<>();

        for (Map.Entry<String, List<FlightCancellationPassengerModel>> entry : map.entrySet()) {
            FlightCancellationModel flightCancellationViewModel = new FlightCancellationModel();

            FlightCancellationJourney flightCancellationJourney = new FlightCancellationJourney();
            flightCancellationJourney.setJourneyId(entry.getKey());

            flightCancellationViewModel.setFlightCancellationJourney(flightCancellationJourney);
            flightCancellationViewModel.setPassengerViewModelList(entry.getValue());

            flightCancellationViewModelList.add(flightCancellationViewModel);
        }

        return flightCancellationViewModelList;
    }

    public FlightCancellationPassengerModel transform(Passenger passenger) {

        FlightCancellationPassengerModel flightCancellationPassengerViewModel = new FlightCancellationPassengerModel();
        flightCancellationPassengerViewModel.setPassengerId(passenger.getPassengerId());
        flightCancellationPassengerViewModel.setTitle(passenger.getTitle());
        flightCancellationPassengerViewModel.setType(passenger.getType());
        flightCancellationPassengerViewModel.setFirstName(passenger.getFirstName());
        flightCancellationPassengerViewModel.setLastName(passenger.getLastName());
        flightCancellationPassengerViewModel.setRelationId(passenger.getRelationId());
        flightCancellationPassengerViewModel.setRelations(passenger.getRelations());
        flightCancellationPassengerViewModel.setStatus(passenger.getStatus());
        flightCancellationPassengerViewModel.setStatusString(passenger.getStatusString());

        return flightCancellationPassengerViewModel;
    }
}
