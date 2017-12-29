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

    int FACEBOOK_AUTHORIZATION_EXCEPTION = 1121;
    int FACEBOOK_EXCEPTION = 1122;
    int EMPTY_ACCESS_TOKEN = 1123;
    int GOOGLE_FAILED_ACCESS_TOKEN = 1124;
    int WEBVIEW_ERROR = 1125;
}
