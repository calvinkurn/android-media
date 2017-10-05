package com.tokopedia.posapp.domain.model;

/**
 * Created by okasurya on 9/27/17.
 */

public class CheckPasswordDomain {
    private boolean status;
    private String message;
    private String state;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
