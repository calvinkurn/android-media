package com.tokopedia.checkout.view.feature.cartlist.viewmodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class XcartParam {
    @SerializedName("products")
    private List<Products> products;

    public XcartParam() {
        products = new ArrayList<>();
    }

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
        private int productId;
        @SerializedName("source_shop_id")
        private int sourceShopId;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getSourceShopId() {
            return sourceShopId;
        }

        public void setSourceShopId(int sourceShopId) {
            this.sourceShopId = sourceShopId;
        }
    }
}
