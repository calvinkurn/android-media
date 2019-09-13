package com.tokopedia.imagesearch.search.exception;

/**
 * @author anggaprasetiyo on 5/26/17.
 */

public class ServerErrorRequestDeniedException extends ServerErrorException {
    private static final long serialVersionUID = -4362332155403290603L;

    public ServerErrorRequestDeniedException(String message, String errorBody,
                                             int errorCode, String url) {
        super(message, errorBody, errorCode, url);
    }
}
