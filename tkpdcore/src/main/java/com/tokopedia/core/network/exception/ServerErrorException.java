package com.tokopedia.core.network.exception;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 5/23/17.
 */

public class ServerErrorException extends IOException {
    private static final long serialVersionUID = -485843615949890545L;

    protected String errorBody;
    protected int errorCode;
    protected String url;

    public ServerErrorException(String message, String errorBody, int errorCode, String url) {
        super(message == null || message.isEmpty()
                ? "Terjadi Kesalahan, mohon coba kembali" : message);
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
