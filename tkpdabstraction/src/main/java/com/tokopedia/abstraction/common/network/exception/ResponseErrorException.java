package com.tokopedia.abstraction.common.network.exception;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;

import java.io.IOException;

/**
 * Created by kris on 3/29/18. Tokopedia
 */

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
