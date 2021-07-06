package com.tokopedia.review.feature.inbox.buyerreview.network;

public class ErrorMessageException extends RuntimeException {

    public static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";

    public ErrorMessageException(String errorMessage) {
        super(errorMessage);
    }

    public ErrorMessageException(String errorMessage, int errorCode) {
        super(errorMessage + " " + "( " + errorCode + " )");
    }

}
