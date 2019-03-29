package com.tokopedia.flight.orderlist.view.viewmodel.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.common.util.FlightStatusOrderType;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderFailedViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderInProcessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderRefundViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderWaitingForPaymentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/14/17.
 */

public class FlightOrderViewModelMapper {
    @Inject
    public FlightOrderViewModelMapper() {
    }

    public List<Visitable> transform(List<FlightOrder> flightOrders) {
        List<Visitable> visitables = new ArrayList<>();
        for (FlightOrder flightOrder : flightOrders) {
            switch (flightOrder.getStatus()) {
                case FlightStatusOrderType.CONFIRMED:
                case FlightStatusOrderType.FINISHED:
                    FlightOrderSuccessViewModel successViewModel = new FlightOrderSuccessViewModel();
                    successViewModel.setCreateTime(flightOrder.getCreateTime());
                    successViewModel.setId(flightOrder.getId());
                    successViewModel.setOrderJourney(flightOrder.getJourneys());
                    successViewModel.setTitle(flightOrder.getStatusString());
                    successViewModel.setCancellations(flightOrder.getCancellations());
                    successViewModel.setStatus(flightOrder.getStatus());
                    successViewModel.setPdf(flightOrder.getPdf());
                    visitables.add(successViewModel);
                    break;
                case FlightStatusOrderType.EXPIRED:
                case FlightStatusOrderType.FAILED:
                    FlightOrderFailedViewModel failedViewModel = new FlightOrderFailedViewModel();
                    failedViewModel.setCreateTime(flightOrder.getCreateTime());
                    failedViewModel.setId(flightOrder.getId());
                    failedViewModel.setOrderJourney(flightOrder.getJourneys());
                    failedViewModel.setTitle(flightOrder.getStatusString());
                    visitables.add(failedViewModel);
                    break;
                case FlightStatusOrderType.READY_FOR_QUEUE:
                case FlightStatusOrderType.PROGRESS:
                    FlightOrderInProcessViewModel inProcessViewModel = new FlightOrderInProcessViewModel();
                    inProcessViewModel.setCreateTime(flightOrder.getCreateTime());
                    inProcessViewModel.setId(flightOrder.getId());
                    inProcessViewModel.setOrderJourney(flightOrder.getJourneys());
                    inProcessViewModel.setStatus(flightOrder.getStatus());
                    inProcessViewModel.setTitle(flightOrder.getStatusString());
                    visitables.add(inProcessViewModel);
                    break;
                case FlightStatusOrderType.WAITING_FOR_THIRD_PARTY:
                case FlightStatusOrderType.WAITING_FOR_PAYMENT:
                case FlightStatusOrderType.WAITING_FOR_TRANSFER:
                    FlightOrderWaitingForPaymentViewModel waitingForPaymentViewModel = new FlightOrderWaitingForPaymentViewModel();
                    waitingForPaymentViewModel.setCreateTime(flightOrder.getCreateTime());
                    waitingForPaymentViewModel.setId(flightOrder.getId());
                    waitingForPaymentViewModel.setOrderJourney(flightOrder.getJourneys());
                    waitingForPaymentViewModel.setStatus(flightOrder.getStatus());
                    waitingForPaymentViewModel.setTitle(flightOrder.getStatusString());
                    waitingForPaymentViewModel.setPayment(flightOrder.getPayment());
                    visitables.add(waitingForPaymentViewModel);
                    break;
                case FlightStatusOrderType.FLIGHT_CANCELLED:
                case FlightStatusOrderType.REFUNDED:
                    FlightOrderRefundViewModel refundViewModel = new FlightOrderRefundViewModel();
                    refundViewModel.setCreateTime(flightOrder.getCreateTime());
                    refundViewModel.setId(flightOrder.getId());
                    refundViewModel.setOrderJourney(flightOrder.getJourneys());
                    refundViewModel.setStatus(flightOrder.getStatus());
                    refundViewModel.setTitle(flightOrder.getStatusString());
                    visitables.add(refundViewModel);
                    break;
            }
        }
        return visitables;
    }
}
