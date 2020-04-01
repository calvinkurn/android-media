package com.tokopedia.opportunity.presentation;

/**
 * Created by hangnadi on 3/3/17.
 */

public class ActionViewData {
    private boolean success;
    private String message;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
