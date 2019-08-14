package com.tokopedia.purchase_platform.checkout.view.subscriber;

import com.tokopedia.purchase_platform.checkout.view.ShipmentContract;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShipmentItemData;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 25/09/18.
 */

public class GetRatesSubscriber extends Subscriber<ShipmentDetailData> {

    private final ShipmentContract.View view;
    private final int shipperId;
    private final int spId;
    private final int itemPosition;

    public GetRatesSubscriber(ShipmentContract.View view, int shipperId, int spId, int itemPosition) {
        this.view = view;
        this.shipperId = shipperId;
        this.spId = spId;
        this.itemPosition = itemPosition;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.renderCourierStateFailed(itemPosition);
    }

    @Override
    public void onNext(ShipmentDetailData shipmentDetailData) {
        if (shipmentDetailData != null && shipmentDetailData.getShipmentItemData() != null) {
            for (ShipmentItemData shipmentItemData : shipmentDetailData.getShipmentItemData()) {
                for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                    if (courierItemData.getShipperId() == shipperId && courierItemData.getShipperProductId() == spId) {
                        view.renderCourierStateSuccess(courierItemData, itemPosition);
                        return;
                    }
                }
            }
        }
        view.renderCourierStateFailed(itemPosition);
    }

}
