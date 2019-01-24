package com.tkpd.library.utils.network;

/**
 * Created by normansyahputa on 1/16/17.
 * this is runtime exception, happened if too many request return from server.
 */
public class ManyRequestErrorException extends RuntimeException {

    public ManyRequestErrorException(String message) {
        super(message);
    }
}
