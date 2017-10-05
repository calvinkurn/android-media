
package com.tokopedia.digital.tokocash.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoCashCashBackModel {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ResponseCashBack data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("config")
    @Expose
    private Object config;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResponseCashBack getData() {
        return data;
    }

    public void setData(ResponseCashBack data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

}
