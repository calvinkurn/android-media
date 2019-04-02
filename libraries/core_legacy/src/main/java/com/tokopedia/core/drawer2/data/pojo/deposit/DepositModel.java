package com.tokopedia.core.drawer2.data.pojo.deposit;

/**
 * Created by nisie on 5/5/17.
 */

public class DepositModel {

    private boolean success;
    private DepositData depositData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isResponseSuccess() {
        return responseCode == 200;
    }

    public DepositData getDepositData() {
        return depositData;
    }

    public void setDepositData(DepositData depositData) {
        this.depositData = depositData;
    }
}
