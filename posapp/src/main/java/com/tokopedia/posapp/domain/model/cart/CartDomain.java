package com.tokopedia.posapp.domain.model.cart;

import com.tokopedia.core.base.domain.DefaultParams;
import com.tokopedia.posapp.domain.model.product.ProductDomain;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartDomain implements DefaultParams {
    private Long id;
    private int productId;
    private ProductDomain product;
    private String outletId;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
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
