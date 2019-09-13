package com.tokopedia.imagesearch.search.exception;

public class ServerErrorMaintenanceException extends ServerErrorException {
    private static final long serialVersionUID = 4240451163303238617L;

    public ServerErrorMaintenanceException(String message, String errorBody,
                                           int errorCode, String url) {
        super(message, errorBody, errorCode, url);
    }
}
