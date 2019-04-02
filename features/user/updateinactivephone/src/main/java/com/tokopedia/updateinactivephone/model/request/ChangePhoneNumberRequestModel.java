package com.tokopedia.updateinactivephone.model.request;

public class ChangePhoneNumberRequestModel {

    private boolean success;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;
    private UploadImageModel uploadIdImageModel;
    private UploadImageModel uploadBankBookImageModel;
    private SubmitImageModel submitImageModel;
    private UploadHostModel uploadHostModel;

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


    public void setUploadIdImageModel(UploadImageModel uploadIdImageModel) {
        this.uploadIdImageModel = uploadIdImageModel;
    }

    public UploadImageModel getUploadIdImageModel() {
        return uploadIdImageModel;
    }

    public void setUploadBankBookImageModel(UploadImageModel uploadBankBookImageModel) {
        this.uploadBankBookImageModel = uploadBankBookImageModel;
    }

    public UploadImageModel getUploadBankBookImageModel() {
        return uploadBankBookImageModel;
    }

    public UploadHostModel getUploadHostModel() {
        return uploadHostModel;
    }

    public void setUploadHostModel(UploadHostModel uploadHostModel) {
        this.uploadHostModel = uploadHostModel;
    }

    public void setSubmitImageModel(SubmitImageModel submitImageModel) {
        this.submitImageModel = submitImageModel;
    }

    public SubmitImageModel getSubmitImageModel() {
        return submitImageModel;
    }
}
