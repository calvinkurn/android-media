package com.tokopedia.core.profile.model;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoDomainModel {

    private boolean success;
    private GetUserInfoDomainData getUserInfoDomainData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public GetUserInfoDomainData getGetUserInfoDomainData() {
        return getUserInfoDomainData;
    }

    public void setGetUserInfoDomainData(GetUserInfoDomainData getUserInfoDomainData) {
        this.getUserInfoDomainData = getUserInfoDomainData;
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
