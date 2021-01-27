package com.tokopedia.review.feature.inbox.buyerreview.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class OrderDataDomain {

    private String invoiceRefNum;
    private String createTimeFmt;
    private String invoiceUrl;

    public OrderDataDomain(String invoiceRefNum, String createTimeFmt,
                           String invoiceUrl) {
        this.invoiceRefNum = invoiceRefNum;
        this.createTimeFmt = createTimeFmt;
        this.invoiceUrl = invoiceUrl;
    }

    public String getInvoiceRefNum() {
        return invoiceRefNum;
    }

    public String getCreateTimeFmt() {
        return createTimeFmt;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }
}
