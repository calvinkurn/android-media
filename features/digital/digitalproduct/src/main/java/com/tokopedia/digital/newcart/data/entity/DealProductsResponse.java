package com.tokopedia.digital.newcart.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DealProductsResponse {
    @SerializedName("products")
    @Expose
    private List<DealProductEntity> products;
    @SerializedName("page")
    @Expose
    private DealPagingEntity page;

    public List<DealProductEntity> getProducts() {
        return products;
    }

    public DealPagingEntity getPage() {
        return page;
    }
}
