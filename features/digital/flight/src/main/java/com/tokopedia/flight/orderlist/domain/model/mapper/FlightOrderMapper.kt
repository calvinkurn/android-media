package com.tokopedia.flight.orderlist.domain.model.mapper

import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity
import com.tokopedia.flight.orderlist.domain.model.FlightOrder
import javax.inject.Inject

class FlightOrderMapper @Inject
constructor(private val flightOrderJourneyMapper: FlightOrderJourneyMapper,
            private val passengerViewModelMapper: FlightOrderPassengerViewModelMapper,
            private val flightInsuranceMapper: FlightInsuranceMapper) {

    fun transform(orderEntity: OrderEntity): FlightOrder {
        return FlightOrder(
                orderEntity.id,
                orderEntity.attributes.status,
                orderEntity.attributes.statusFmt,
                orderEntity.attributes.createTime,
                orderEntity.attributes.flight.email,
                orderEntity.attributes.flight.phone,
                orderEntity.attributes.flight.totalAdult,
                orderEntity.attributes.flight.totalAdultNumeric,
                orderEntity.attributes.flight.totalChild,
                orderEntity.attributes.flight.totalChildNumeric,
                orderEntity.attributes.flight.totalInfant,
                orderEntity.attributes.flight.totalInfantNumeric,
                orderEntity.attributes.flight.invoiceUri,
                orderEntity.attributes.flight.eticketUri,
                orderEntity.attributes.flight.currency,
                orderEntity.attributes.flight.pdf,
                flightOrderJourneyMapper.transform(orderEntity.attributes.flight.journeys),
                passengerViewModelMapper.transform(orderEntity.attributes.flight.passengers,
                        orderEntity.attributes.flight.cancellations),
                orderEntity.attributes.flight.payment,
                orderEntity.attributes.flight.cancellations,
                flightInsuranceMapper.transform(orderEntity.attributes.flight.insurances),
                passengerViewModelMapper.getCancelledPassengerCount(orderEntity.attributes
                        .flight.cancellations),
                orderEntity.attributes.flight.contactUsUrl)
    }

    fun transform(orderEntities: List<OrderEntity>): List<FlightOrder> {
        return orderEntities.map {
            return@map transform(it)
        }
    }
}
