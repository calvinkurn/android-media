package com.tokopedia.posapp.domain.model;

/**
 * Created by okasurya on 10/11/17.
 */

public class CreateOrderDomain {
    private boolean status;
    private int orderId;
    private String invoiceRef;

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

    public String getInvoiceRef() {
        return invoiceRef;
    }

    public void setInvoiceRef(String invoiceRef) {
        this.invoiceRef = invoiceRef;
    }
}
