package com.tokopedia.posapp.product.productlist.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okasurya on 4/13/18.
 */

public class ProductList {
    @SerializedName("products")
    @Expose
    private List<ProductDetail> products;
    @SerializedName("status")
    @Expose
    private String status;

    public List<ProductDetail> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetail> products) {
        this.products = products;
    }
}
