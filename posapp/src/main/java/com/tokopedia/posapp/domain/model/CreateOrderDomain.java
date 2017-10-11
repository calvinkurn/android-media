package com.tokopedia.posapp.domain.model;

/**
 * Created by okasurya on 10/11/17.
 */

public class CreateOrderDomain {
    private boolean status;
    private int orderId;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
