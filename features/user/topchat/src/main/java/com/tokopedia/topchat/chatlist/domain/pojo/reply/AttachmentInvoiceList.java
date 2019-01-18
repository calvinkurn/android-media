package com.tokopedia.topchat.chatlist.domain.pojo.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendri on 05/04/18.
 */

public class AttachmentInvoiceList {
    @SerializedName("invoices")
    @Expose
    private List<AttachmentInvoice> invoices;

    public List<AttachmentInvoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<AttachmentInvoice> invoices) {
        this.invoices = invoices;
    }
}
