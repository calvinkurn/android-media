package com.tokopedia.home.account.analytics.data.model.topcash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashModel {

    @SerializedName("success")
    @Expose
    private boolean success = false;
    @SerializedName("data")
    @Expose
    private TokoCashData data = new TokoCashData();
    @SerializedName("error")
    @Expose
    private String errorMessage = "";
    @SerializedName("status")
    @Expose
    private String statusMessage = "";
    @SerializedName("code")
    @Expose
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public TokoCashData getData() {
        return data;
    }

    public void setData(TokoCashData data) {
        this.data = data;
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
