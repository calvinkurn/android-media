package com.tokopedia.core.network.retrofit.response;

/**
 * Created by Angga.Prasetiyo on 01/12/2015.
 */

@Deprecated
public interface ResponseStatus {
    int SC_BAD_GATEWAY = 502;
    int SC_BAD_REQUEST = 400;
    int SC_FORBIDDEN = 403;
    int SC_GATEWAY_TIMEOUT = 504;
    int SC_INTERNAL_SERVER_ERROR = 500;
    int SC_OK = 200;
    int SC_REQUEST_TIMEOUT = 408;
}
