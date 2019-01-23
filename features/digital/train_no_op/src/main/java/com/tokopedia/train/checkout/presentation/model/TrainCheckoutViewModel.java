package com.tokopedia.train.checkout.presentation.model;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutViewModel {

    private String redirectURL;
    private String callbackURLSuccess;
    private String callbackURLFailed;
    private String queryString;
    private String transactionId;

    public TrainCheckoutViewModel(String redirectURL, String callbackURLSuccess,
                                  String callbackURLFailed, String queryString, String transactionId) {
        this.redirectURL = redirectURL;
        this.callbackURLSuccess = callbackURLSuccess;
        this.callbackURLFailed = callbackURLFailed;
        this.queryString = queryString;
        this.transactionId = transactionId;
    }

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