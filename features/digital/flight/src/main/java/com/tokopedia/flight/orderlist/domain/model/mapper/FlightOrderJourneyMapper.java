package com.tokopedia.flight.orderlist.domain.model.mapper;

import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModelMapper;
import com.tokopedia.flight.orderlist.data.cloud.entity.JourneyEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/11/17.
 */

public class FlightOrderJourneyMapper {
    private FlightDetailRouteViewModelMapper flightDetailRouteViewModelMapper;

    @Inject
    public FlightOrderJourneyMapper(FlightDetailRouteViewModelMapper flightDetailRouteViewModelMapper) {
        this.flightDetailRouteViewModelMapper = flightDetailRouteViewModelMapper;
    }

    public FlightOrderJourney transform(JourneyEntity journeyEntity) {
        FlightOrderJourney flightOrderJourney = null;
        if (journeyEntity != null) {
            flightOrderJourney = new FlightOrderJourney();
            flightOrderJourney.setJourneyId(journeyEntity.getId());
            flightOrderJourney.setDepartureAiportId(journeyEntity.getDepartureAirportId());
            flightOrderJourney.setDepartureTime(journeyEntity.getDepartureTime());
            flightOrderJourney.setDepartureCity(journeyEntity.getDepartureCityName());
            flightOrderJourney.setArrivalTime(journeyEntity.getArrivalTime());
            flightOrderJourney.setArrivalCity(journeyEntity.getArrivalCityName());
            flightOrderJourney.setArrivalAirportId(journeyEntity.getArrivalAirportId());
            flightOrderJourney.setRouteViewModels(flightDetailRouteViewModelMapper.transformList(journeyEntity.getRoutes()));
            flightOrderJourney.setStatus(String.valueOf(journeyEntity.getStatus()));
        }
        return flightOrderJourney;
    }

    public List<FlightOrderJourney> transform(List<JourneyEntity> journeyEntities) {
        List<FlightOrderJourney> journeys = new ArrayList<>();
        FlightOrderJourney journey = null;
        if (journeyEntities != null) {
            for (JourneyEntity entity : journeyEntities) {
                journey = transform(entity);
                if (journey != null) {
                    journeys.add(journey);
                }
            }
        }
        return journeys;
    }
}
