package com.tokopedia.payment.fingerprint.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public class ResponsePaymentFingerprint {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("param_encode")
    @Expose
    private String paramEncode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParamEncode() {
        return paramEncode;
    }

    public void setParamEncode(String paramEncode) {
        this.paramEncode = paramEncode;
    }
}
