package com.tokopedia.sessioncommon.network;

import java.io.IOException;

/**
 * @author by nisie on 10/22/18.
 */
public class TokenErrorException extends IOException {


    private final String error;
    private final String errorDescription;

    public TokenErrorException(String error, String errorDescription, String errorKey) {
        super(errorDescription + " " + "( " + errorKey + " )");

        this.error = error;
        this.errorDescription = errorDescription;

    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
