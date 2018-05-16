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

public class TrackingPageMapper implements ITrackingPageMapper{

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
        model.setStatus(order.getStatus());

        List<TrackingHistoryViewModel> trackingHistoryViewModels = new ArrayList<>();
        for (int i = 0; i < order.getTrackHistory().size(); i++) {
            TrackingHistoryViewModel historyViewModel = new TrackingHistoryViewModel();
            historyViewModel.setCity(order.getTrackHistory().get(i).getCity());
            historyViewModel.setStatus(order.getTrackHistory().get(i).getStatus());
            historyViewModel.setTime(order.getTrackHistory().get(i).getDate());
            historyViewModel.setTitle(order.getTrackHistory().get(i).getStatus());
            trackingHistoryViewModels.add(historyViewModel);
            if(i == 0) historyViewModel.setColor("#42b549");
            else historyViewModel.setColor("#000000");
        }
        model.setHistoryList(trackingHistoryViewModels);
        return model;
    }

    private boolean switchInteger(int value) {
        return value == 1;
    }
}
