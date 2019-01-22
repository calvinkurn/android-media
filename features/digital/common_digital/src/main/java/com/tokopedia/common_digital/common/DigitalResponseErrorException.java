package com.tokopedia.common_digital.common;

import com.tokopedia.network.constant.ErrorNetMessage;

import java.io.IOException;

/**
 * Created by Rizky on 13/08/18.
 */
public class DigitalResponseErrorException extends IOException {

    private String messageError;

    public DigitalResponseErrorException() {
        super("Http Error : " + ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
    }

    public DigitalResponseErrorException(String message) {
        super("Http Error : " + message);
        if (message != null && !message.isEmpty()) this.messageError = message;
        else this.messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
    }

    @Override
    public String getMessage() {
        return messageError;
    }

}
