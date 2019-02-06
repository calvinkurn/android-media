package com.tokopedia.core.network.retrofit.exception;

import com.tokopedia.core.network.exception.ServerErrorException;

/**
 * @author anggaprasetiyo on 5/26/17.
 */

@Deprecated
public class ServerErrorTimeZoneException extends ServerErrorException {

    private static final long serialVersionUID = -1892335256541668911L;

    public ServerErrorTimeZoneException(String message, String errorBody,
                                        int errorCode, String url) {
        super(message, errorBody, errorCode, url);
    }
}
