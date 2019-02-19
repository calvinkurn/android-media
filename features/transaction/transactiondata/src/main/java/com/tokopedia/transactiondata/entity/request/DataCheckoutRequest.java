package com.tokopedia.transactiondata.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class DataCheckoutRequest {
    @SerializedName("address_id")
    @Expose
    public int addressId;
    @SerializedName("shop_products")
    @Expose
    public List<ShopProductCheckoutRequest> shopProducts = new ArrayList<>();

    public DataCheckoutRequest() {
    }

    private DataCheckoutRequest(Builder builder) {
        addressId = builder.addressId;
        shopProducts = builder.shopProducts;
    }


    public static final class Builder {
        private int addressId;
        private List<ShopProductCheckoutRequest> shopProducts;

        public Builder() {
        }

        public Builder addressId(int val) {
            addressId = val;
            return this;
        }

        public Builder shopProducts(List<ShopProductCheckoutRequest> val) {
            shopProducts = val;
            return this;
        }

        public DataCheckoutRequest build() {
            return new DataCheckoutRequest(this);
        }
    }
}
