package com.tokopedia.gm.featured.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductSubmitModel {
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("items_featured")
    @Expose
    private List<ItemsFeatured> itemsFeatured = null;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<ItemsFeatured> getItemsFeatured() {
        return itemsFeatured;
    }

    public void setItemsFeatured(List<ItemsFeatured> itemsFeatured) {
        this.itemsFeatured = itemsFeatured;
    }

    public static class ItemsFeatured {

        @SerializedName("product_id")
        @Expose
        private long productId;
        @SerializedName("order")
        @Expose
        private long order;
        @SerializedName("type")
        @Expose
        private long type;

        public long getProductId() {
            return productId;
        }

        public void setProductId(long productId) {
            this.productId = productId;
        }

        public long getOrder() {
            return order;
        }

        public void setOrder(long order) {
            this.order = order;
        }

        public long getType() {
            return type;
        }

        public void setType(long type) {
            this.type = type;
        }

    }
}
