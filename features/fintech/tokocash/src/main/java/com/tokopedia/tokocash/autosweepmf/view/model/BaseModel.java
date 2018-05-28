package com.tokopedia.tokocash.autosweepmf.view.model;

/**
 * Base model class for all sub model
 */
public class BaseModel {
    protected boolean success;
    protected String message;
    protected int code;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
