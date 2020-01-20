package com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class ShipmentStateProductData {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_preorder")
    @Expose
    private ShipmentStateProductPreorder productPreorder;

    public ShipmentStateProductData(Builder builder) {
        productId = builder.productId;
        productPreorder = builder.productPreorder;
    }

    public static final class Builder {
        private int productId;
        private ShipmentStateProductPreorder productPreorder;

        public Builder() {
        }

        public Builder productId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder productPreorder(ShipmentStateProductPreorder productPreorder) {
            this.productPreorder = productPreorder;
            return this;
        }

        public ShipmentStateProductData build() {
            return new ShipmentStateProductData(this);
        }
    }

}
