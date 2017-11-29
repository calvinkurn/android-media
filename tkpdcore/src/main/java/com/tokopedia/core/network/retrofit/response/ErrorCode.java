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
    int EMPTY_PROVIDER = 1100;
    int FACEBOOK_AUTHORIZATION_EXCEPTION = 1121;
    int FACEBOOK_EXCEPTION = 1122;
    int EMPTY_ACCESS_TOKEN = 1123;
    int ERROR_BUNDLE_WEBVIEW = 1124;

    int UNKNOWN_SECURITY_QUESTION_TYPE = 1125;
    int UNSUPPORTED_FLOW = 1126;
}
