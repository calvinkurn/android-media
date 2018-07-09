package com.tokopedia.posapp.base.domain.model;

/**
 * Created by okasurya on 9/15/17.
 */

public class DataStatus {
    public static int OK = 1;
    public static int ERROR = 2;

    private int status;
    private String message;

    public static DataStatus defaultOkResult() {
        DataStatus dataStatus = new DataStatus();
        dataStatus.setStatus(OK);
        dataStatus.setMessage("OK");
        return dataStatus;
    }

    public static DataStatus defaultErrorResult(Throwable e) {
        DataStatus dataStatus = new DataStatus();
        dataStatus.setStatus(ERROR);
        dataStatus.setMessage(e.getMessage());
        return dataStatus;
    }

    public static DataStatus defaultErrorResult(String errorMessage) {
        DataStatus dataStatus = new DataStatus();
        dataStatus.setStatus(ERROR);
        dataStatus.setMessage(errorMessage);
        return dataStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOk() {
        return status == OK;
    }
}
