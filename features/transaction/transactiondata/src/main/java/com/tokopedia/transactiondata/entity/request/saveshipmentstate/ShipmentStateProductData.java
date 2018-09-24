package com.tokopedia.transactiondata.entity.request.saveshipmentstate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class ShipmentStateProductData {

    @SerializedName("product_id")
    @Expose
    private int productId;

    public ShipmentStateProductData(Builder builder) {
        productId = builder.productId;
    }

    public static final class Builder {
        private int productId;

        public Builder() {
        }

        public Builder productId(int productId) {
            this.productId = productId;
            return this;
        }

        public ShipmentStateProductData build() {
            return new ShipmentStateProductData(this);
        }
    }

}
