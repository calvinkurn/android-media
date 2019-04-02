package com.tokopedia.otp.common.network;

import java.io.IOException;

/**
 * Created by nisie on 3/14/17.
 */

public class ErrorMessageException extends IOException {

    private String errorCode;

    public ErrorMessageException(String errorMessage) {
        super(errorMessage);
    }

    public ErrorMessageException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
