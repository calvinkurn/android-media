package com.tokopedia.digital.tokocash.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 10/9/17.
 */

public class WalletTokenEntity {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("responseCode")
    @Expose
    private int responseCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
