package com.tokopedia.core.react;

/**
 * @author ricoharisin .
 */

public class PromiseException extends Exception {

    protected String errorCode;

    public PromiseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
