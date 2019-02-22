package com.tokopedia.flight.orderlist.domain.model.mapper

import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity
import com.tokopedia.flight.orderlist.domain.model.FlightOrder
import javax.inject.Inject

class FlightOrderMapper @Inject
constructor(private val flightOrderJourneyMapper: FlightOrderJourneyMapper,
            private val passengerViewModelMapper: FlightOrderPassengerViewModelMapper,
            private val flightInsuranceMapper: FlightInsuranceMapper) {

    fun transform(orderEntities: List<OrderEntity>): List<FlightOrder> {
        return orderEntities.map {
            return@map FlightOrder(
                    it.id,
                    it.attributes.status,
                    it.attributes.statusFmt,
                    it.attributes.createTime,
                    it.attributes.flight.email,
                    it.attributes.flight.phone,
                    it.attributes.flight.totalAdult,
                    it.attributes.flight.totalAdultNumeric,
                    it.attributes.flight.totalChild,
                    it.attributes.flight.totalChildNumeric,
                    it.attributes.flight.totalInfant,
                    it.attributes.flight.totalInfantNumeric,
                    it.attributes.flight.invoiceUri,
                    it.attributes.flight.eticketUri,
                    it.attributes.flight.currency,
                    it.attributes.flight.pdf,
                    flightOrderJourneyMapper.transform(it.attributes.flight.journeys),
                    passengerViewModelMapper.transform(it.attributes.flight.passengers,
                            it.attributes.flight.cancellations),
                    it.attributes.flight.payment,
                    it.attributes.flight.cancellations,
                    flightInsuranceMapper.transform(it.attributes.flight.insurances),
                    passengerViewModelMapper.getCancelledPassengerCount(it.attributes
                            .flight.cancellations),
                    it.attributes.flight.contactUsUrl)
        }
    }
}
