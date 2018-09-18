package com.tokopedia.updateinactivephone.model.request;


import com.tokopedia.updateinactivephone.model.response.UploadImageData;

public class UploadImageModel {
    private boolean success;
    private UploadImageData uploadImageData;
    private String errorMessage;
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

    public boolean isResponseSuccess() {
        return responseCode == 200;
    }

    public UploadImageData getUploadImageData() {
        return uploadImageData;
    }

    public void setUploadImageData(UploadImageData uploadImageData) {
        this.uploadImageData = uploadImageData;
    }

}
