package com.tokopedia.digital.exception;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class InvalidCartDataException extends RuntimeException {
    public InvalidCartDataException(String message) {
        super(message);
    }
}
