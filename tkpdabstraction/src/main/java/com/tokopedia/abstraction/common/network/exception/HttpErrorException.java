package com.tokopedia.abstraction.common.network.exception;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.constant.ResponseStatus;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 07/05/18.
 */
public class HttpErrorException extends IOException {

    private static final long serialVersionUID = -7454251067196613612L;
    private final int errorCode;
    private String messageError;

    public HttpErrorException(int errorCode) {
        super("Http Error : " + errorCode);
        this.errorCode = errorCode;
        switch (errorCode) {
            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                messageError = ErrorNetMessage.MESSAGE_ERROR_SERVER;
                break;
            case ResponseStatus.SC_FORBIDDEN:
                messageError = ErrorNetMessage.MESSAGE_ERROR_FORBIDDEN;
                break;
            case ResponseStatus.SC_REQUEST_TIMEOUT:
            case ResponseStatus.SC_GATEWAY_TIMEOUT:
                messageError = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                break;
            default:
                messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                break;
        }

    }

    @Override
    public String getMessage() {
        return messageError;
    }

    public int getErrorCode() {
        return errorCode;
    }
}