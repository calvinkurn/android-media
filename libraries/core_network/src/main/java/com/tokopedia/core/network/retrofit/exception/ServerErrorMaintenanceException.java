package com.tokopedia.core.network.retrofit.exception;

import com.tokopedia.core.network.exception.ServerErrorException;

/**
 * @author anggaprasetiyo on 5/26/17.
 */

@Deprecated
public class ServerErrorMaintenanceException extends ServerErrorException {
    private static final long serialVersionUID = 4240451163303238617L;

    public ServerErrorMaintenanceException(String message, String errorBody,
                                           int errorCode, String url) {
        super(message, errorBody, errorCode, url);
    }
}
