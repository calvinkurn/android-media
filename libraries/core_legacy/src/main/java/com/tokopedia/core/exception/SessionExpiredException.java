package com.tokopedia.core.exception;

import java.io.IOException;

/**
 * Created by kris on 1/11/17. Tokopedia
 */

public class SessionExpiredException extends IOException {

    public SessionExpiredException() {
        super();
    }

    public SessionExpiredException(String errorMessage) {
        super("Expiry Message : " + errorMessage);
    }
}
