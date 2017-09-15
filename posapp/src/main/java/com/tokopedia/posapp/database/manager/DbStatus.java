package com.tokopedia.posapp.database.manager;

/**
 * Created by okasurya on 9/15/17.
 */

public class DbStatus {
    public static int OK = 1;
    public static int ERROR = 2;

    private int status;
    private String message;

    public static DbStatus createOkResult() {
        DbStatus dbStatus = new DbStatus();
        dbStatus.setStatus(OK);
        dbStatus.setMessage("OK");
        return dbStatus;
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
