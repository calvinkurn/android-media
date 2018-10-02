package com.tokopedia.loginregister.common.util;

import android.content.Context;
import com.tokopedia.loginregister.R;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;

/**
 * @author by nisie on 10/2/18.
 */
public class ErrorHandlerSession extends ErrorHandler {

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


    public interface ErrorForbiddenListener {
        void onForbidden();
        void onError(String errorMessage);
    }

    public static void getErrorMessage(ErrorForbiddenListener listener, Throwable e, Context context) {
        String forbiddenMessage = context.getString(R.string.default_request_error_forbidden_auth);
        String errorMessage = getErrorMessage(context, e);
        if (errorMessage.equals(forbiddenMessage)) {
            listener.onForbidden();
        } else {
            listener.onError(errorMessage);
        }
    }

    public static String getDefaultErrorCodeMessage(int errorCode, Context context) {
        return context.getString(R.string.default_request_error_unknown)
                + " (" + errorCode + ")";
    }

}
