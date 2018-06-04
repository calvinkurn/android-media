package com.tokopedia.tracking.mapper;

import com.tokopedia.tracking.entity.TrackOrder;
import com.tokopedia.tracking.entity.TrackingResponse;
import com.tokopedia.tracking.viewmodel.TrackingHistoryViewModel;
import com.tokopedia.tracking.viewmodel.TrackingViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public class TrackingPageMapper implements ITrackingPageMapper {

    @Override
    public TrackingViewModel trackingViewModel(TrackingResponse trackingResponse) {
        TrackingViewModel model = new TrackingViewModel();
        TrackOrder order = trackingResponse.getTrackOrder();
        model.setInvalid(switchInteger(order.getInvalid()));
        model.setBuyerAddress(order.getDetail().getReceiverCity());
        model.setBuyerName(order.getDetail().getReceiverName());
        model.setDeliveryDate(order.getDetail().getSendDate());
        model.setReferenceNumber(order.getShippingRefNum());
        model.setSellerStore(order.getDetail().getShipperName());
        model.setSellerAddress(order.getDetail().getShipperCity());
        model.setServiceCode(order.getDetail().getServiceCode());
        model.setChange(order.getChange());
        model.setStatus(order.getStatus());

        List<TrackingHistoryViewModel> trackingHistoryViewModels = new ArrayList<>();
        for (int i = 0; i < order.getTrackHistory().size(); i++) {
            TrackingHistoryViewModel historyViewModel = new TrackingHistoryViewModel();
            historyViewModel.setCity(order.getTrackHistory().get(i).getCity());
            historyViewModel.setStatus(order.getTrackHistory().get(i).getStatus());
            historyViewModel.setTime(order.getTrackHistory().get(i).getDate());
            splitDate(historyViewModel, order, i);
            historyViewModel.setTitle(order.getTrackHistory().get(i).getStatus());
            trackingHistoryViewModels.add(historyViewModel);
            if (i == 0) historyViewModel.setColor("#42b549");
            else historyViewModel.setColor("#9B9B9B");
        }
        model.setHistoryList(trackingHistoryViewModels);
        return model;
    }

    private void splitDate(TrackingHistoryViewModel historyViewModel, TrackOrder order, int i) {
        String[] splitDate = order.getTrackHistory().get(i).getDate().split("-");
        historyViewModel.setYear(splitDate[0] != null ? splitDate[0] : "");
        historyViewModel.setMonth(splitDate[1] != null ? splitDate[1] : "");
        historyViewModel.setYear(splitDate[2] != null ? splitDate[2] : "");
    }

    private boolean switchInteger(int value) {
        return value == 1;
    }
}
