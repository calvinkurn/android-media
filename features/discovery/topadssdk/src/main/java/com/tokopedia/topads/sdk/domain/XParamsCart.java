package com.tokopedia.topads.sdk.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author errysuprayogi on 23,November,2018
 */
public class XParamsCart {

    @SerializedName("products")
    private List<Products> products;

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public static class Products {
        /**
         * product_id : 326464243
         * source_shop_id : 917717
         */

        @SerializedName("product_id")
        private String productId;
        @SerializedName("source_shop_id")
        private String sourceShopId;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getSourceShopId() {
            return sourceShopId;
        }

        public void setSourceShopId(String sourceShopId) {
            this.sourceShopId = sourceShopId;
        }
    }
}
