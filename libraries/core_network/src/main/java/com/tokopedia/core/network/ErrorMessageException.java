package com.tokopedia.core.network;

/**
 * Created by nisie on 3/14/17.
 */

@Deprecated
public class ErrorMessageException extends RuntimeException {

    public static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";

    public ErrorMessageException(String errorMessage) {
        super(errorMessage);
    }

    public ErrorMessageException(String errorMessage, int errorCode) {
        super(errorMessage + " " + CoreNetworkApplication.getAppContext().getString(R.string.code_error)
                + " " + errorCode);
    }
}
