package com.tokopedia.flight.orderlist.domain.model.mapper

import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModelMapper
import com.tokopedia.flight.orderlist.data.cloud.entity.JourneyEntity
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney
import javax.inject.Inject

/**
 * Created by alvarisi on 12/11/17.
 */

class FlightOrderJourneyMapper @Inject
constructor(private val flightDetailRouteViewModelMapper: FlightDetailRouteViewModelMapper) {

    fun transform(journeyEntity: JourneyEntity): FlightOrderJourney {
        return FlightOrderJourney(
                journeyEntity.id,
                journeyEntity.departureCityName,
                "",
                journeyEntity.departureAirportId,
                journeyEntity.departureTime,
                journeyEntity.arrivalCityName,
                "",
                journeyEntity.arrivalAirportId,
                journeyEntity.arrivalTime,
                journeyEntity.status.toString(),
                flightDetailRouteViewModelMapper.transformList(journeyEntity.routes))
    }

    fun transform(journeyEntities: List<JourneyEntity>): List<FlightOrderJourney> {
        return journeyEntities.map {
            return@map transform(it)
        }
    }
}
