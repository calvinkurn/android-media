package com.tokopedia.imagesearch.search.exception;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 5/23/17.
 */

public class ServerErrorException extends IOException {
    private static final String MESSAGE_ERROR_DEFAULT = "Terjadi kesalahan, ulangi beberapa saat lagi";
    private static final long serialVersionUID = -485843615949890545L;

    protected String errorBody;
    protected int errorCode;
    protected String url;

    public ServerErrorException(String message, String errorBody, int errorCode, String url) {
        super(message == null || message.isEmpty()
                ? MESSAGE_ERROR_DEFAULT : message);
        this.errorBody = errorBody;
        this.errorCode = errorCode;
        this.url = url;
    }

    public String getErrorBody() {
        return errorBody;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getUrl() {
        return url;
    }
}
