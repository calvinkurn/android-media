package com.tokopedia.core.network;

/**
 * Created by nisie on 3/14/17.
 */

public class ErrorMessageException extends RuntimeException {

    public static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    public static final String ERROR_MESSAGE = "message_error";

    public ErrorMessageException(String errorMessage) {
        super(errorMessage);
    }
}
