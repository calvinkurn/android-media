package com.tokopedia.transactiondata.entity.request.saveshipmentstate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/09/18.
 */

public class ShipmentStateShopProductData {

    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("is_preorder")
    @Expose
    private int isPreorder;
    @SerializedName("warehouse_id")
    @Expose
    private int warehouseId;
    @SerializedName("finsurance")
    @Expose
    private int finsurance;
    @SerializedName("shipping_info")
    @Expose
    private ShipmentStateShippingInfoData shippingInfoData;
    @SerializedName("is_dropship")
    @Expose
    private int isDropship;
    @SerializedName("dropship_data")
    @Expose
    private ShipmentStateDropshipData dropshipData;
    @SerializedName("is_order_priority")
    @Expose
    private int isOrderPriority;
    @SerializedName("product_data")
    @Expose
    private List<ShipmentStateProductData> productDataList;

    public ShipmentStateShopProductData(Builder builder) {
        shopId = builder.shopId;
        isPreorder = builder.isPreorder;
        warehouseId = builder.warehouseId;
        finsurance = builder.finsurance;
        shippingInfoData = builder.shippingInfoData;
        isDropship = builder.isDropship;
        dropshipData = builder.dropshipData;
        isOrderPriority = builder.isOrderPriority;
        productDataList = builder.productDataList;
    }

    public static final class Builder {
        private int shopId;
        private int isPreorder;
        private int warehouseId;
        private int finsurance;
        private ShipmentStateShippingInfoData shippingInfoData;
        private int isDropship;
        private int isOrderPriority;
        private ShipmentStateDropshipData dropshipData;
        private List<ShipmentStateProductData> productDataList;

        public Builder() {
        }

        public Builder shopId(int shopId) {
            this.shopId = shopId;
            return this;
        }

        public Builder isPreorder(int isPreorder) {
            this.isPreorder = isPreorder;
            return this;
        }

        public Builder warehouseId(int warehouseId) {
            this.warehouseId = warehouseId;
            return this;
        }

        public Builder finsurance(int finsurance) {
            this.finsurance = finsurance;
            return this;
        }

        public Builder shippingInfoData(ShipmentStateShippingInfoData shippingInfoData) {
            this.shippingInfoData = shippingInfoData;
            return this;
        }

        public Builder isDropship(int isDropship) {
            this.isDropship = isDropship;
            return this;
        }

        public Builder isOrderPriority(int isOrderPriority) {
            this.isOrderPriority = isOrderPriority;
            return this;
        }

        public Builder dropshipData(ShipmentStateDropshipData dropshipData) {
            this.dropshipData = dropshipData;
            return this;
        }

        public Builder productDataList(List<ShipmentStateProductData> productDataList) {
            this.productDataList = productDataList;
            return this;
        }

        public ShipmentStateShopProductData build() {
            return new ShipmentStateShopProductData(this);
        }



    }

}
