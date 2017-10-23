package com.tokopedia.posapp.data.pojo.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 10/11/17.
 */

public class PaymentAction {
    @SerializedName("order_id")
    @Expose
    private int orderId;

    @SerializedName("invoice_ref")
    @Expose
    private String invoiceRef;

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
