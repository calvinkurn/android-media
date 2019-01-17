package com.tokopedia.flight.orderlist.domain.model;

import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightInsuranceMapper;
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderJourneyMapper;
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderPassengerViewModelMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/11/17.
 */

public class FlightOrderMapper {
    private FlightOrderJourneyMapper flightOrderJourneyMapper;
    private FlightOrderPassengerViewModelMapper passengerViewModelMapper;
    private FlightInsuranceMapper flightInsuranceMapper;

    @Inject
    public FlightOrderMapper(FlightOrderJourneyMapper flightOrderJourneyMapper,
                             FlightOrderPassengerViewModelMapper passengerViewModelMapper,
                             FlightInsuranceMapper flightInsuranceMapper) {
        this.flightOrderJourneyMapper = flightOrderJourneyMapper;
        this.passengerViewModelMapper = passengerViewModelMapper;
        this.flightInsuranceMapper = flightInsuranceMapper;
    }

    public FlightOrder transform(OrderEntity orderEntity) {
        FlightOrder flightOrder = null;
        if (orderEntity != null) {
            flightOrder = new FlightOrder();
            flightOrder.setId(orderEntity.getId());
            flightOrder.setStatus(orderEntity.getAttributes().getStatus());
            flightOrder.setPdf(orderEntity.getAttributes().getFlight().getPdf());
            flightOrder.setStatusString(orderEntity.getAttributes().getStatusFmt());
            flightOrder.setCreateTime(orderEntity.getAttributes().getCreateTime());
            flightOrder.setEmail(orderEntity.getAttributes().getFlight().getEmail());
            flightOrder.setCurrency(orderEntity.getAttributes().getFlight().getCurrency());
            flightOrder.setTelp(orderEntity.getAttributes().getFlight().getPhone());
            flightOrder.setTotalAdult(orderEntity.getAttributes().getFlight().getTotalAdult());
            flightOrder.setTotalAdultNumeric(orderEntity.getAttributes().getFlight().getTotalAdultNumeric());
            flightOrder.setTotalChild(orderEntity.getAttributes().getFlight().getTotalChild());
            flightOrder.setTotalChildNumeric(orderEntity.getAttributes().getFlight().getTotalChildNumeric());
            flightOrder.setTotalInfant(orderEntity.getAttributes().getFlight().getTotalInfant());
            flightOrder.setTotalInfantNumeric(orderEntity.getAttributes().getFlight().getTotalInfantNumeric());
            flightOrder.setJourneys(flightOrderJourneyMapper.transform(orderEntity.getAttributes().getFlight().getJourneys()));
            flightOrder.setPassengerViewModels(passengerViewModelMapper.transform(orderEntity.getAttributes().getFlight().getPassengers(),
                    orderEntity.getAttributes().getFlight().getCancellations()));
            flightOrder.setCancelledPassengerCount(passengerViewModelMapper.getCancelledPassengerCount(orderEntity.getAttributes()
                    .getFlight().getCancellations()));
            flightOrder.setPayment(orderEntity.getAttributes().getFlight().getPayment());
            flightOrder.setCancellations(orderEntity.getAttributes().getFlight().getCancellations());
            flightOrder.setInsurances(flightInsuranceMapper.transform(orderEntity.getAttributes().getFlight().getInsurances()));
            flightOrder.setContactUsUrl(orderEntity.getAttributes().getFlight().getContactUsUrl());
        }
        return flightOrder;
    }

    public List<FlightOrder> transform(List<OrderEntity> orderEntities) {
        List<FlightOrder> flightOrders = new ArrayList<>();
        FlightOrder flightOrder;
        if (orderEntities != null) {
            for (OrderEntity orderEntity : orderEntities) {
                flightOrder = transform(orderEntity);
                if (flightOrder != null) {
                    flightOrders.add(flightOrder);
                }
            }
        }
        return flightOrders;
    }
}
