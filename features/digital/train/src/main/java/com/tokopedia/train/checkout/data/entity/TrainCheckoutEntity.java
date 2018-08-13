package com.tokopedia.train.checkout.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 26/07/18.
 */
public class TrainCheckoutEntity {

    @SerializedName("redirectURL")
    @Expose
    private String redirectURL;

    @SerializedName("callbackURLSuccess")
    @Expose
    private String callbackURLSuccess;

    @SerializedName("callbackURLFailed")
    @Expose
    private String callbackURLFailed;

    @SerializedName("thanksURL")
    @Expose
    private String thanksURL;

    @SerializedName("queryString")
    @Expose
    private String queryString;

    @SerializedName("parameter")
    @Expose
    private TrainCheckoutParameterEntity parameter;

    public String getRedirectURL() {
        return redirectURL;
    }

    public String getCallbackURLSuccess() {
        return callbackURLSuccess;
    }

    public String getCallbackURLFailed() {
        return callbackURLFailed;
    }

    public String getThanksURL() {
        return thanksURL;
    }

    public String getQueryString() {
        return queryString;
    }

    public TrainCheckoutParameterEntity getParameter() {
        return parameter;
    }
}