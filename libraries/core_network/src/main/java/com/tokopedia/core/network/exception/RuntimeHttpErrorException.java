package com.tokopedia.core.network.exception;

import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

@Deprecated
public class RuntimeHttpErrorException extends RuntimeException {

    private static final long serialVersionUID = -3093170622024881475L;

    private final int errorCode;
    private String messageError;

    public RuntimeHttpErrorException(int errorCode) {
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
