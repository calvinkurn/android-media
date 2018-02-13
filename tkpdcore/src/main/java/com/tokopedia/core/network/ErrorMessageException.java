package com.tokopedia.core.network;

import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;

/**
 * Created by nisie on 3/14/17.
 */

public class ErrorMessageException extends RuntimeException {

    public static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    public static final String ERROR_MESSAGE = "message_error";

    public ErrorMessageException(String errorMessage) {
        super(errorMessage);
    }

    public ErrorMessageException(String errorMessage, int errorCode) {
        super(errorMessage + " " + MainApplication.getAppContext().getString(R.string.code_error)
                + " " + errorCode);
    }
}
