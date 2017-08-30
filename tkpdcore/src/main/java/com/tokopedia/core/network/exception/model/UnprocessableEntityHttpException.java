package com.tokopedia.core.network.exception.model;

import java.io.IOException;

/**
 * Created by alvarisi on 3/16/17.
 */

public class UnprocessableEntityHttpException extends IOException {
    public UnprocessableEntityHttpException() {
        super("Request data is invalid, please check message");
    }

    public UnprocessableEntityHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprocessableEntityHttpException(String message) {
        super(message);
    }
}

