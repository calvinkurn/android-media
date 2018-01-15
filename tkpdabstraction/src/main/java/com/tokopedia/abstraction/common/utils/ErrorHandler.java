package com.tokopedia.abstraction.common.utils;

import android.content.Context;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.common.network.constant.ResponseStatus;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by User on 11/28/2017.
 */

public class ErrorHandler {

    public static String getErrorMessage(final Context context, Throwable e) {
        if (e instanceof UnknownHostException) {
            return context.getString(R.string.msg_no_connection);
        } else if (e instanceof SocketTimeoutException) {
            return context.getString(R.string.default_request_error_timeout);
        } else if (e instanceof IOException) {
            return context.getString(R.string.default_request_error_internal_server);
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                !e.getLocalizedMessage().equals("") &&
                e.getLocalizedMessage().length() <= 3) {
            int code = Integer.parseInt(e.getLocalizedMessage());
            switch (code) {
                case ResponseStatus.SC_REQUEST_TIMEOUT:
                case ResponseStatus.SC_GATEWAY_TIMEOUT:
                     return context.getString(R.string.default_request_error_timeout);
                case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                    return context.getString(R.string.default_request_error_internal_server);
                case ResponseStatus.SC_FORBIDDEN:
                    return context.getString(R.string.default_request_error_forbidden_auth);
                case ResponseStatus.SC_BAD_GATEWAY:
                    return context.getString(R.string.default_request_error_bad_request);
                case ResponseStatus.SC_BAD_REQUEST:
                    return context.getString(R.string.default_request_error_bad_request);
                default:
                    return context.getString(R.string.default_request_error_unknown);
            }
        } else {
            return context.getString(R.string.default_request_error_unknown);
        }
    }
}
