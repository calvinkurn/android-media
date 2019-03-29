package com.tokopedia.vote.network;

import java.io.IOException;

/**
 * @author by nisie on 5/7/18.
 */
public class VoteErrorException extends IOException {
    private String errorMessage;
    private String errorCode;

    public VoteErrorException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
