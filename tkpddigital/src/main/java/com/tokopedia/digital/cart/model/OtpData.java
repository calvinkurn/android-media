package com.tokopedia.digital.cart.model;

/**
 * @author anggaprasetiyo on 3/20/17.
 */

public class OtpData {
    private String message;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
