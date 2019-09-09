package com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.purchase_platform.common.data.model.request.checkout.RatesFeature;

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
    @SerializedName("rates_feature")
    @Expose
    private RatesFeature ratesFeature;

    public ShipmentStateShippingInfoData(Builder builder) {
        shippingId = builder.shippingId;
        spId = builder.spId;
        ratesFeature = builder.ratesFeature;
    }

    public static final class Builder {
        private int shippingId;
        private int spId;
        private RatesFeature ratesFeature;

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

        public Builder ratesFeature(RatesFeature ratesFeature) {
            this.ratesFeature = ratesFeature;
            return this;
        }

        public ShipmentStateShippingInfoData build() {
            return new ShipmentStateShippingInfoData(this);
        }
    }
}
