package com.tokopedia.digital.common.exception;

/**
 * @author anggaprasetiyo on 3/8/17.
 */

public class MapperDataException extends RuntimeException {

    private static final long serialVersionUID = 6327730569976065381L;

    public MapperDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
