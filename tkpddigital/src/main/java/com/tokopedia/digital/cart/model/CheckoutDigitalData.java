package com.tokopedia.digital.cart.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class CheckoutDigitalData {

    private String successCallbackUrl;
    private String failedCallbackUrl;
    private String redirectUrl;
    private String transactionId;
    private String stringQuery;

    public String getSuccessCallbackUrl() {
        return successCallbackUrl;
    }

    public void setSuccessCallbackUrl(String successCallbackUrl) {
        this.successCallbackUrl = successCallbackUrl;
    }

    public String getFailedCallbackUrl() {
        return failedCallbackUrl;
    }

    public void setFailedCallbackUrl(String failedCallbackUrl) {
        this.failedCallbackUrl = failedCallbackUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStringQuery() {
        return stringQuery;
    }

    public void setStringQuery(String stringQuery) {
        this.stringQuery = stringQuery;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
