package com.tokopedia.updateinactivephone.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadHostModel {

    private boolean success;
    private UploadHostData uploadHostData;
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

    public UploadHostData getUploadHostData() {
        return uploadHostData;
    }

    public void setUploadHostData(UploadHostData uploadHostData) {
        this.uploadHostData = uploadHostData;
    }


    public class UploadHostData {
        @SerializedName("generated_host")
        @Expose
        GeneratedHost generatedHost;

        public GeneratedHost getGeneratedHost() {
            return generatedHost;
        }

        public void setGeneratedHost(GeneratedHost generatedHost) {
            this.generatedHost = generatedHost;
        }
    }

    public class GeneratedHost {
        @SerializedName("server_id")
        @Expose
        int serverId;
        @SerializedName("upload_host")
        @Expose
        String uploadHost;

        public int getServerId() {
            return serverId;
        }

        public void setServerId(int serverId) {
            this.serverId = serverId;
        }

        public String getUploadHost() {
            return uploadHost;
        }

        public void setUploadHost(String uploadHost) {
            this.uploadHost = uploadHost;
        }
    }
}
