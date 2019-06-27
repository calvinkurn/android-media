package com.tokopedia.train.checkout.presentation.model;

public class TrainCheckoutViewModel {

    private String redirectURL;
    private String callbackURLSuccess;
    private String callbackURLFailed;
    private String queryString;
    private String transactionId;

    public String getRedirectURL() {
        return redirectURL;
    }

    public String getCallbackURLSuccess() {
        return callbackURLSuccess;
    }

    public String getCallbackURLFailed() {
        return callbackURLFailed;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getTransactionId() {
        return transactionId;
    }
}