package com.tokopedia.flight.review.view.model;

/**
 * Created by alvarisi on 12/20/17.
 */

public class FlightCheckoutViewModel {
    private String paymentId;
    private String queryString;
    private String redirectUrl;
    private String callbackSuccessUrl;
    private String callbackFailedUrl;
    private String transactionId;

    public FlightCheckoutViewModel() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getCallbackSuccessUrl() {
        return callbackSuccessUrl;
    }

    public void setCallbackSuccessUrl(String callbackSuccessUrl) {
        this.callbackSuccessUrl = callbackSuccessUrl;
    }

    public String getCallbackFailedUrl() {
        return callbackFailedUrl;
    }

    public void setCallbackFailedUrl(String callbackFailedUrl) {
        this.callbackFailedUrl = callbackFailedUrl;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
