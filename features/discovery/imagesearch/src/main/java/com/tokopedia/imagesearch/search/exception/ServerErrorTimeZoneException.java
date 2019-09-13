package com.tokopedia.imagesearch.search.exception;

public class ServerErrorTimeZoneException extends ServerErrorException {

    private static final long serialVersionUID = -1892335256541668911L;

    public ServerErrorTimeZoneException(String message, String errorBody,
                                        int errorCode, String url) {
        super(message, errorBody, errorCode, url);
    }
}
