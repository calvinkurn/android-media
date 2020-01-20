package com.tokopedia.purchase_platform.features.checkout.data.model.request.saveshipmentstate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class ShipmentStateRequestData {

    @SerializedName("address_id")
    @Expose
    private int addressId;
    @SerializedName("shop_products")
    @Expose
    private List<ShipmentStateShopProductData> shopProductDataList;

    public ShipmentStateRequestData(Builder builder) {
        addressId = builder.addressId;
        shopProductDataList = builder.shopProductDataList;
    }

    public static final class Builder {
        private int addressId;
        private List<ShipmentStateShopProductData> shopProductDataList;

        public Builder() {
        }

        public Builder addressId(int addressId) {
            this.addressId = addressId;
            return this;
        }

        public Builder shopProductDataList(List<ShipmentStateShopProductData> shopProductDataList) {
            this.shopProductDataList = shopProductDataList;
            return this;
        }

        public ShipmentStateRequestData build() {
            return new ShipmentStateRequestData(this);
        }
    }
}
