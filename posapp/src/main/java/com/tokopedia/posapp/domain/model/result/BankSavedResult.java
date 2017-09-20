package com.tokopedia.posapp.domain.model.result;

/**
 * Created by okasurya on 9/8/17.
 */

public class BankSavedResult {
    private boolean status;
    private String message;

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
}
