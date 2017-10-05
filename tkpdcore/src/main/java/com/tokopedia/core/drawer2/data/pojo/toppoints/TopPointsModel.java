package com.tokopedia.core.drawer2.data.pojo.toppoints;

/**
 * Created by nisie on 5/5/17.
 */

public class TopPointsModel {

    private boolean success;
    private TopPointsData topPointsData;
    private String errorMessage;
    private String statusMessage;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public TopPointsData getTopPointsData() {
        return topPointsData;
    }

    public void setTopPointsData(TopPointsData topPointsData) {
        this.topPointsData = topPointsData;
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
