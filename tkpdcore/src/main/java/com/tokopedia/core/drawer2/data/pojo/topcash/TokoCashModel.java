package com.tokopedia.core.drawer2.data.pojo.topcash;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashModel {

    private boolean success;
    private TokoCashData tokoCashData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public TokoCashData getTokoCashData() {
        return tokoCashData;
    }

    public void setTokoCashData(TokoCashData tokoCashData) {
        this.tokoCashData = tokoCashData;
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
