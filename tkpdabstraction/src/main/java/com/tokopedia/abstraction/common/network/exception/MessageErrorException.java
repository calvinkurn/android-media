package com.tokopedia.abstraction.common.network.exception;

import java.io.IOException;

/**
 * Created by normansyahputa on 1/16/17.
 * this class represent message error delivered by web service.
 */
public class MessageErrorException extends IOException {
    public MessageErrorException(String message) {
        super(message);
    }

    public MessageErrorException() {
    }
}
