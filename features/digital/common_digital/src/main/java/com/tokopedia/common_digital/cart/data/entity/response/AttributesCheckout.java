package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class AttributesCheckout {
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
    private Parameter parameter;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getCallbackUrlSuccess() {
        return callbackUrlSuccess;
    }

    public String getCallbackUrlFailed() {
        return callbackUrlFailed;
    }

    public String getQueryString() {
        return queryString;
    }

    public Parameter getParameter() {
        return parameter;
    }
}
