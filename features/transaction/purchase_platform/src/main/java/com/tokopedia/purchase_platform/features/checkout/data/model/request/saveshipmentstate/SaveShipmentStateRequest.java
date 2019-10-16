package com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class SaveShipmentStateRequest {

    private List<ShipmentStateRequestData> requestDataList;

    public List<ShipmentStateRequestData> getRequestDataList() {
        return requestDataList;
    }

    public SaveShipmentStateRequest(Builder builder) {
        requestDataList = builder.requestDataList;
    }

    public static final class Builder {
        List<ShipmentStateRequestData> requestDataList;

        public Builder requestDataList(List<ShipmentStateRequestData> requestDataList) {
            this.requestDataList = requestDataList;
            return this;
        }

        public SaveShipmentStateRequest build() {
            return new SaveShipmentStateRequest(this);
        }
    }
}
