package com.tokopedia.core.network.retrofit.response;

/**
 * @author by nisie on 10/10/17.
 */

public interface ErrorCode {
    int UNKNOWN = 1000;
    int UNKNOWN_HOST_EXCEPTION = 1001;
    int SOCKET_TIMEOUT_EXCEPTION = 1002;
    int IO_EXCEPTION = 1003;
    int WS_ERROR = 1004;
    int UNSUPPORTED_FLOW = 1005;
}
