package com.tokopedia.abstraction.common.network.exception;

import java.io.IOException;

/**
 * Created by normansyahputa on 1/16/17.
 * this class represent message error delivered by web service.
 * Use {com.tokopedia.network.exception.MessageErrorException} instead of this class
 */
@Deprecated
public class MessageErrorException extends IOException {
    private String errorCode;

    public MessageErrorException(String message) {
        super(message);
    }

    public MessageErrorException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public MessageErrorException() {
    }
}
