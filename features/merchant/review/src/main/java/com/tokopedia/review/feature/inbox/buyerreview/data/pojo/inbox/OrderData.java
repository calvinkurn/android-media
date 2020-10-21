
package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderData {

    @SerializedName("invoice_ref_num")
    @Expose
    private String invoiceRefNum;
    @SerializedName("create_time_fmt")
    @Expose
    private String createTimeFmt;
    @SerializedName("invoice_url")
    @Expose
    private String invoiceUrl;

    public String getInvoiceRefNum() {
        return invoiceRefNum;
    }

    public void setInvoiceRefNum(String invoiceRefNum) {
        this.invoiceRefNum = invoiceRefNum;
    }

    public String getCreateTimeFmt() {
        return createTimeFmt;
    }

    public void setCreateTimeFmt(String createTimeFmt) {
        this.createTimeFmt = createTimeFmt;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

}
