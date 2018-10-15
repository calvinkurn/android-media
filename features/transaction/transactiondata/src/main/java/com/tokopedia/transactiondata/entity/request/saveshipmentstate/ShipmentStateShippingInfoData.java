package com.tokopedia.transactiondata.entity.request.saveshipmentstate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class ShipmentStateShippingInfoData {

    @SerializedName("shipping_id")
    @Expose
    private int shippingId;
    @SerializedName("sp_id")
    @Expose
    private int spId;

    public ShipmentStateShippingInfoData(Builder builder) {
        shippingId = builder.shippingId;
        spId = builder.spId;
    }

    public static final class Builder {
        private int shippingId;
        private int spId;

        public Builder() {
        }

        public Builder shippingId(int shippingId) {
            this.shippingId = shippingId;
            return this;
        }

        public Builder spId(int spId) {
            this.spId = spId;
            return this;
        }

        public ShipmentStateShippingInfoData build() {
            return new ShipmentStateShippingInfoData(this);
        }
    }
}
