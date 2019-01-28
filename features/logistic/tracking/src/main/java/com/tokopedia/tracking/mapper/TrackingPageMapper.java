package com.tokopedia.tracking.mapper;

import com.tokopedia.logisticdata.data.entity.trackingshipment.Detail;
import com.tokopedia.logisticdata.data.entity.trackingshipment.TrackOrder;
import com.tokopedia.logisticdata.data.entity.trackingshipment.TrackingResponse;
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
        model.setReferenceNumber(order.getShippingRefNum());

        Detail detailOrder = order.getDetail();
        model.setBuyerAddress(
                detailOrder == null || order.getDetail().getReceiverCity() == null ?
                        "" : order.getDetail().getReceiverCity()
        );
        model.setBuyerName(
                detailOrder == null || order.getDetail().getReceiverName() == null ?
                        "" : order.getDetail().getReceiverName()
        );
        model.setDeliveryDate(
                detailOrder == null || order.getDetail().getSendDate() == null ?
                        "" : order.getDetail().getSendDate()
        );
        model.setSellerStore(
                detailOrder == null || order.getDetail().getShipperName() == null ?
                        "" : order.getDetail().getShipperName()
        );
        model.setSellerAddress(
                detailOrder == null || order.getDetail().getShipperCity() == null ?
                        "" : order.getDetail().getShipperCity()
        );
        model.setServiceCode(
                detailOrder == null || order.getDetail().getServiceCode() == null ?
                        "" : order.getDetail().getServiceCode()
        );

        model.setChange(order.getChange());
        model.setStatus(order.getStatus());
        model.setStatusNumber(order.getOrderStatus());

        List<TrackingHistoryViewModel> trackingHistoryViewModels = new ArrayList<>();
        if (order.getTrackHistory() != null && !order.getTrackHistory().isEmpty()) {
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
