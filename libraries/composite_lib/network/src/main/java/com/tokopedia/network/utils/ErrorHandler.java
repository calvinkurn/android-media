package com.tokopedia.network.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.network.R;
import com.tokopedia.network.constant.ResponseStatus;
import com.tokopedia.network.data.model.response.ResponseV4ErrorException;
import com.tokopedia.network.exception.MessageErrorException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ErrorHandler {

    public static String getErrorMessage(final Context context, Throwable e) {
        String errorMessageString = getErrorMessageString(context, e);
        String errorCodeNative = ExceptionDictionary.Companion.getErrorCodeSimple(e);
        String errorCodeHTTP = getErrorMessageHTTP(context, e);

        String errorMessage = errorMessageString + " " + errorCodeNative;
        return errorMessage;
    }

    public static String getErrorMessageString(final Context context, Throwable e) {
        if (context == null || e == null) {
            return "Terjadi kesalahan. Ulangi beberapa saat lagi";
        }

        if (e instanceof ResponseV4ErrorException) {
            return ((ResponseV4ErrorException) e).getErrorList().get(0);
        } else if (e instanceof UnknownHostException) {
            return context.getString(R.string.msg_no_connection);
        } else if (e instanceof SocketTimeoutException) {
            return context.getString(R.string.default_request_error_timeout);
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                !e.getLocalizedMessage().equals("") &&
                e.getLocalizedMessage().length() <= 3) {
            try {
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
                    case ResponseStatus.SC_UNAUTHORIZED:
                        return context.getString(R.string.msg_expired_session_or_unauthorized);
                    default:
                        return context.getString(R.string.default_request_error_unknown);
                }
            } catch (NumberFormatException e1) {
                return context.getString(R.string.default_request_error_unknown);
            }
        } else if (e instanceof MessageErrorException && !TextUtils.isEmpty(e.getMessage())) {
            return e.getMessage();
        } else if (e instanceof IOException) {
            return context.getString(R.string.default_request_error_internal_server);
        } else {
            return context.getString(R.string.default_request_error_unknown);
        }
    }

    public static String getErrorMessageHTTP(final Context context, Throwable e) {
        if (e instanceof MessageErrorException && !TextUtils.isEmpty(e.getMessage())) {
            return ((MessageErrorException) e).getErrorCode();
        } else {
            return "000";
        }
    }
}
