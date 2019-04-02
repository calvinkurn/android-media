package com.tokopedia.flight.review.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 12/20/17.
 */

public class FlightCheckoutAttributesEntity {
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("callback_url_success")
    @Expose
    private String callbackUrlSuccess;
    @SerializedName("callback_url_failed")
    @Expose
    private String callbackUrlFailed;
    @SerializedName("query_string")
    @Expose
    private String queryString;
    @SerializedName("parameter")
    @Expose
    private FlightCheckoutParameterEntity parameter;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getCallbackUrlSuccess() {
        return callbackUrlSuccess;
    }

    public void setCallbackUrlSuccess(String callbackUrlSuccess) {
        this.callbackUrlSuccess = callbackUrlSuccess;
    }

    public String getCallbackUrlFailed() {
        return callbackUrlFailed;
    }

    public void setCallbackUrlFailed(String callbackUrlFailed) {
        this.callbackUrlFailed = callbackUrlFailed;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public FlightCheckoutParameterEntity getParameter() {
        return parameter;
    }

    public void setParameter(FlightCheckoutParameterEntity parameter) {
        this.parameter = parameter;
    }
}
