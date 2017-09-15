package com.tokopedia.posapp.domain.model.cart;

import com.tokopedia.core.base.domain.DefaultParams;

/**
 * Created by okasurya on 8/22/17.
 */

public class CartDomain implements DefaultParams {
    private Long id;
    private String productId;
    private String outletId;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
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
}
