package com.tokopedia.core.network.exception;

import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 3/4/17.
 */

@Deprecated
public class ResponseErrorException extends IOException {

    private static final long serialVersionUID = -3848721958439593398L;

    private String messageError;

    public ResponseErrorException() {
        super("Http Error : " + ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
    }

    public ResponseErrorException(String message) {
        super("Http Error : " + message);
        if (message != null && !message.isEmpty()) this.messageError = message;
        else this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
    }


    @Override
    public String getMessage() {
        return messageError;
    }
}
