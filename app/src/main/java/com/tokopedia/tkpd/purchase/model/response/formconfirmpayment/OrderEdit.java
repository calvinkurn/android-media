package com.tokopedia.tkpd.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo
 * on 30/06/2016.
 */
public class OrderEdit {

    @SerializedName("order_invoice_string")
    @Expose
    private String orderInvoiceString;
    @SerializedName("order_invoice")
    @Expose
    private List<String> orderInvoiceList = new ArrayList<>();

    public String getOrderInvoiceString() {
        return orderInvoiceString;
    }

    public void setOrderInvoiceString(String orderInvoiceString) {
        this.orderInvoiceString = orderInvoiceString;
    }

    public List<String> getOrderInvoiceList() {
        return orderInvoiceList;
    }

    public void setOrderInvoiceList(List<String> orderInvoiceList) {
        this.orderInvoiceList = orderInvoiceList;
    }
}
