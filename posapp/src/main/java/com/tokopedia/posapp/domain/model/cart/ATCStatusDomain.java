package com.tokopedia.posapp.domain.model.cart;

/**
 * Created by okasurya on 8/22/17.
 */

public class ATCStatusDomain {
    public static final int RESULT_ADD_TO_CART_RUNNING = 1;
    public static final int RESULT_ADD_TO_CART_ERROR = 2;
    public static final int RESULT_ADD_TO_CART_SUCCESS = 3;
    public static final int RESULT_ADD_TO_CART_TIMEOUT = 4;
    public static final int RESULT_ADD_TO_CART_NO_CONNECTION = 5;

    public static final String DEFAULT_SUCCESS_MESSAGE = "Success";
    public static final String DEFAULT_ERROR_MESSAGE = "Error";

    private int status;
    private String message;

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
}
