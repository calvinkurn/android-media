package com.tokopedia.core.profile.model;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoDomainModel {

    private boolean success;
    private GetUserInfoDomainData getUserInfoDomainData;
    private String errorMessage;
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
}
