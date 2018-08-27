package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/23/17.
 */

public class AttributesInstantCheckout {

    @SerializedName("thanks_url")
    @Expose
    private String thanksUrl;
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

    public String getThanksUrl() {
        return thanksUrl;
    }

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
}
