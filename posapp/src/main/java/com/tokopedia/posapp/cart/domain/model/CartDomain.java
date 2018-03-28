package com.tokopedia.posapp.cart.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.base.domain.DefaultParams;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartDomain implements DefaultParams {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("productId")
    @Expose
    private long productId;
    @SerializedName("product")
    @Expose
    private ProductDomain product;
    @SerializedName("outletId")
    @Expose
    private String outletId;
    @SerializedName("quantity")
    @Expose
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductDomain getProduct() {
        return product;
    }

    public void setProduct(ProductDomain product) {
        this.product = product;
    }
}
