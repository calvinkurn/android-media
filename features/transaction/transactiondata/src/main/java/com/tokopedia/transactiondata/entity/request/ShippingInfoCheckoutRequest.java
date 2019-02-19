package com.tokopedia.transactiondata.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class ShippingInfoCheckoutRequest {

    @SerializedName("shipping_id")
    @Expose
    public int shippingId;
    @SerializedName("sp_id")
    @Expose
    public int spId;
    @SerializedName("rates_id")
    @Expose
    public String ratesId;

    public ShippingInfoCheckoutRequest() {
    }

    private ShippingInfoCheckoutRequest(Builder builder) {
        shippingId = builder.shippingId;
        spId = builder.spId;
        ratesId = builder.ratesId;
    }


    public static final class Builder {
        private int shippingId;
        private int spId;
        private String ratesId;

        public Builder() {
        }

        public Builder shippingId(int val) {
            shippingId = val;
            return this;
        }

        public Builder spId(int val) {
            spId = val;
            return this;
        }

        public Builder ratesId(String val) {
            ratesId = val;
            return this;
        }

        public ShippingInfoCheckoutRequest build() {
            return new ShippingInfoCheckoutRequest(this);
        }
    }
}
