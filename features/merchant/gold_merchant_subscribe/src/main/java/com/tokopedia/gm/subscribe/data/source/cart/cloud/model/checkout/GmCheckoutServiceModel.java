package com.tokopedia.gm.subscribe.data.source.cart.cloud.model.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GmCheckoutServiceModel {

    @SerializedName("paymentURL")
    @Expose
    private String paymentUrl;
    @SerializedName("parameter1")
    @Expose
    private String parameter1;
    @SerializedName("callbackurl")
    @Expose
    private String callbackurl;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("error")
    @Expose
    private List<String> error = null;

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }
}
