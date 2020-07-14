package com.tokopedia.core.drawer2.data.pojo.profile;

/**
 * Created by nisie on 5/5/17.
 */

public class ProfileModel {

    private boolean success;
    private ProfileData profileData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ProfileData getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileData profileData) {
        this.profileData = profileData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
