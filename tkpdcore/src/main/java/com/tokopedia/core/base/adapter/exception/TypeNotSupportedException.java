package com.tokopedia.core.base.adapter.exception;

/**
 * @author kulomady on 1/24/17.
 */
public class TypeNotSupportedException extends RuntimeException {
    private TypeNotSupportedException(String message) {
        super(message);
    }

    public static TypeNotSupportedException create(String message) {
        return new TypeNotSupportedException(message);
    }
}

