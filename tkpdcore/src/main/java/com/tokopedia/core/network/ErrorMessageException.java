package com.tokopedia.core.network;

/**
 * Created by nisie on 3/14/17.
 */

public class ErrorMessageException extends RuntimeException {

    public ErrorMessageException(String errorMessage) {
        super(errorMessage);
    }
}
