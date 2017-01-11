package com.tokopedia.core.exception;

/**
 * Created by kris on 1/11/17. Tokopedia
 */

public class SessionExpiredException extends Exception{

    public SessionExpiredException() {
        super();
    }

    public SessionExpiredException(String errorMessage) {
        super("Expiry Message : " + errorMessage);
    }
}
