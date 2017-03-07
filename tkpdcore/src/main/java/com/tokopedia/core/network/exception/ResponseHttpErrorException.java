package com.tokopedia.core.network.exception;

import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 3/4/17.
 */

public class ResponseHttpErrorException extends IOException {

    private static final long serialVersionUID = -3848721958439593398L;

    private String errorDesc;
    private String messageError;
    private int errorCode;

    public ResponseHttpErrorException(int errorCode) {
        super("Http Error : " + errorCode);
        this.errorCode = errorCode;
        this.errorDesc = "Http Error : " + errorCode;
        defineErrorDescByCode();
    }

    public ResponseHttpErrorException(int code, String message) {
        super("Http Error : " + code);
        this.errorCode = code;
        if (message != null) this.messageError = String.valueOf(message);
        else defineErrorDescByCode();
    }

    private void defineErrorDescByCode() {
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

    public String getErrorDesc() {
        return errorDesc;
    }
}
