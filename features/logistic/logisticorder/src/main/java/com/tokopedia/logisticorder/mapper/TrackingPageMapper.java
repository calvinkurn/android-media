package com.tokopedia.logisticorder.mapper;

import com.tokopedia.logisticCommon.data.entity.trackingshipment.Detail;
import com.tokopedia.logisticCommon.data.entity.trackingshipment.Page;
import com.tokopedia.logisticCommon.data.entity.trackingshipment.TrackOrder;
import com.tokopedia.logisticCommon.data.entity.trackingshipment.TrackingResponse;
import com.tokopedia.logisticorder.uimodel.AdditionalInfoUiModel;
import com.tokopedia.logisticorder.uimodel.TrackingHistoryUiModel;
import com.tokopedia.logisticorder.uimodel.TrackingUiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 5/14/18. Tokopedia
 */

public class TrackingPageMapper implements ITrackingPageMapper {

    @Override
    public TrackingUiModel trackingUiModel(TrackingResponse trackingResponse) {
        TrackingUiModel model = new TrackingUiModel();
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

        List<TrackingHistoryUiModel> trackingHistoryUiModels = new ArrayList<>();
        if (order.getTrackHistory() != null && !order.getTrackHistory().isEmpty()) {
            for (int i = 0; i < order.getTrackHistory().size(); i++) {
                TrackingHistoryUiModel historyUiModel = new TrackingHistoryUiModel();
                historyUiModel.setCity(order.getTrackHistory().get(i).getCity());
                historyUiModel.setStatus(order.getTrackHistory().get(i).getStatus());
                historyUiModel.setDate(order.getTrackHistory().get(i).getDate());
                historyUiModel.setTime(order.getTrackHistory().get(i).getTime());
                splitDate(historyUiModel, order, i);
                historyUiModel.setTitle(order.getTrackHistory().get(i).getStatus());
                trackingHistoryUiModels.add(historyUiModel);
                if (i == 0) historyUiModel.setColor("#42b549");
                else historyUiModel.setColor("#9B9B9B");
            }
        }
        model.setHistoryList(trackingHistoryUiModels);

        if (trackingResponse.getPage() != null) {
            Page trackPage = trackingResponse.getPage();
            List<AdditionalInfoUiModel> listAdditionalInfo = new ArrayList<>();
            if (trackPage.getListAdditionalInfo() != null) {
                if (!trackPage.getListAdditionalInfo().isEmpty()) {
                    for (int i = 0; i < trackPage.getListAdditionalInfo().size(); i++) {
                        AdditionalInfoUiModel additionalInfoUiModel = new AdditionalInfoUiModel(
                                trackPage.getListAdditionalInfo().get(i).getTitle(),
                                trackPage.getListAdditionalInfo().get(i).getNotes(),
                                trackPage.getListAdditionalInfo().get(i).getUrlDetail(),
                                trackPage.getListAdditionalInfo().get(i).getUrlText());
                        listAdditionalInfo.add(additionalInfoUiModel);
                    }
                }
                model.setAdditionalInfoList(listAdditionalInfo);
            }
        }

        model.setTrackingUrl(
                detailOrder == null || order.getDetail().getTrackingUrl() == null ?
                        "" : order.getDetail().getTrackingUrl()
        );

        return model;
    }

    private void splitDate(TrackingHistoryUiModel historyUiModel, TrackOrder order, int i) {
        String[] splitDate = order.getTrackHistory().get(i).getDate().split("-");
        historyUiModel.setYear(splitDate[0] != null ? splitDate[0] : "");
        historyUiModel.setMonth(splitDate[1] != null ? splitDate[1] : "");
        historyUiModel.setYear(splitDate[2] != null ? splitDate[2] : "");
    }

    private boolean switchInteger(int value) {
        return value == 1;
    }
}
