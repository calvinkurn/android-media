package com.tkpd.library.utils.network;

/**
 * Created by normansyahputa on 1/16/17.
 * this class represent message error delivered by web service.
 */
@Deprecated
public class MessageErrorException extends RuntimeException {
    public MessageErrorException(String message) {
        super(message);
    }
}
