package com.tokopedia.flight.cancellation.domain.mapper;

import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationDetail;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListPassengerModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationDetailsAttribute;
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.JourneyEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.RefundDetailEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderJourneyMapper;
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderPassengerViewModelMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.flight.common.util.FlightDateUtil.DEFAULT_VIEW_TIME_FORMAT;
import static com.tokopedia.flight.common.util.FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightOrderEntityToCancellationListMapper {

    private FlightOrderJourneyMapper flightOrderJourneyMapper;
    private FlightOrderPassengerViewModelMapper flightOrderPassengerViewModelMapper;

    @Inject
    public FlightOrderEntityToCancellationListMapper(FlightOrderJourneyMapper flightOrderJourneyMapper,
                                                     FlightOrderPassengerViewModelMapper flightOrderPassengerViewModelMapper) {
        this.flightOrderJourneyMapper = flightOrderJourneyMapper;
        this.flightOrderPassengerViewModelMapper = flightOrderPassengerViewModelMapper;
    }

    public List<FlightCancellationListModel> transform(OrderEntity orderEntity) {
        List<FlightCancellationListModel> cancellationListViewModelList = new ArrayList<>();

        for (CancellationEntity item : orderEntity.getAttributes().getFlight().getCancellations()) {
            FlightCancellationListModel cancellationItem = new FlightCancellationListModel();
            cancellationItem.setOrderId(orderEntity.getId());
            cancellationItem.setCancellations(transform(item, orderEntity));

            cancellationListViewModelList.add(cancellationItem);
        }

        return cancellationListViewModelList;
    }

    private FlightCancellationDetail transform(CancellationEntity cancellation, OrderEntity orderEntity) {
        FlightCancellationDetail cancellationItem = new FlightCancellationDetail();

        cancellationItem.setCreateTime(FlightDateUtil.formatDate(
                YYYY_MM_DD_T_HH_MM_SS_Z, DEFAULT_VIEW_TIME_FORMAT, cancellation.getCreateTime()));
        cancellationItem.setEstimatedRefund(cancellation.getEstimatedRefund());
        cancellationItem.setRealRefund(cancellation.getRealRefund());
        cancellationItem.setRefundId(cancellation.getRefundId());
        cancellationItem.setStatus(cancellation.getStatus());
        cancellationItem.setJourneys(transform(orderEntity.getAttributes().getFlight().getJourneys(),
                cancellation.getDetails()));
        cancellationItem.setPassengers(transformPassenger(orderEntity.getAttributes().getFlight()
                .getPassengers(), cancellation.getDetails()));
        cancellationItem.setStatusStr(cancellation.getStatusStr());
        cancellationItem.setStatusType(cancellation.getStatusType());
        cancellationItem.setRefundInfo(cancellation.getRefundInfo());
        if (cancellation.getRefundDetail() != null &&
                cancellation.getRefundDetail().getTopInfo() != null &&
                cancellation.getRefundDetail().getMiddleInfo() != null &&
                cancellation.getRefundDetail().getBottomInfo() != null) {
            cancellationItem.setRefundDetail(cancellation.getRefundDetail());
        } else {
            cancellationItem.setRefundDetail(new RefundDetailEntity());
        }

        return cancellationItem;
    }

    private List<FlightOrderJourney> transform(List<JourneyEntity> journeys, List<CancellationDetailsAttribute> details) {
        List<FlightOrderJourney> flightOrderJourneyList = new ArrayList<>();
        List<Long> journeyIds = new ArrayList<>();

        for (CancellationDetailsAttribute cancellationItem : details) {
            for (JourneyEntity item : journeys) {
                if (cancellationItem.getJourneyId() == item.getId() && !journeyIds.contains(item.getId())) {
                    flightOrderJourneyList.add(flightOrderJourneyMapper.transform(item));
                    journeyIds.add(item.getId());
                }
            }
        }

        return flightOrderJourneyList;
    }

    private List<FlightCancellationListPassengerModel> transformPassenger(List<PassengerEntity> passengers, List<CancellationDetailsAttribute> details) {
        List<FlightCancellationListPassengerModel> passengerViewModelList = new ArrayList<>();

        for (CancellationDetailsAttribute cancellationItem : details) {
            for (PassengerEntity item : passengers) {
                if (cancellationItem.getPassengerId().equals(item.getId())) {
                    FlightCancellationListPassengerModel passengerItem = new FlightCancellationListPassengerModel();
                    passengerItem.setPassengerId(item.getId());
                    passengerItem.setTitle(item.getTitle());
                    passengerItem.setType(item.getType());
                    passengerItem.setFirstName(item.getFirstName());
                    passengerItem.setLastName(item.getLastName());
                    passengerItem.setAmenities(flightOrderPassengerViewModelMapper.transformAmenities(item.getAmenities()));
                    passengerItem.setJourneyId(cancellationItem.getJourneyId());

                    passengerViewModelList.add(passengerItem);
                }
            }
        }

        return passengerViewModelList;
    }
}
