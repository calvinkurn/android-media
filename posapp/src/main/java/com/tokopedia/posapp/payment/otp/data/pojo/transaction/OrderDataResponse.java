package com.tokopedia.posapp.payment.otp.data.pojo.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 5/3/18.
 */

public class OrderDataResponse {
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("invoice_ref")
    @Expose
    private String invoiceRef;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceRef() {
        return invoiceRef;
    }

    public void setInvoiceRef(String invoiceRef) {
        this.invoiceRef = invoiceRef;
    }
}
