package com.tokopedia.loginphone.common.network;

/**
 * @author by nisie on 29/10/18.
 */
public class TokoCashErrorException extends RuntimeException {
    private final String errorMessage;

    public TokoCashErrorException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
