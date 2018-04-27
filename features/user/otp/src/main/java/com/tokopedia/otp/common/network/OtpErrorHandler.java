package com.tokopedia.otp.common.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException;
import com.tokopedia.abstraction.common.network.constant.ResponseStatus;
import com.tokopedia.otp.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author by Angga.Prasetiyo on 01/12/2015.
 * Edited by Nisie
 */
public class OtpErrorHandler {

    private static final String TAG = OtpErrorHandler.class.getSimpleName();

    private static final String SERVER_INFO = "Network Server Error";
    private static final String FORBIDDEN_INFO = "Network Forbidden";
    private static final String BAD_REQUEST_INFO = "Network Bad Request";
    private static final String UNKNOWN_INFO = "Network Error";
    private static final String TIMEOUT_INFO = "Network Timeout";

    private static String getErrorInfo(int code, String msg) {
        return "Error " + String.valueOf(code) + " : " + msg;
    }

    public static String getErrorMessage(Throwable e, final Context context) {
        if (e instanceof UnknownHostException) {
            return context.getString(R.string.msg_no_connection);
        } else if (e instanceof SocketTimeoutException) {
            return context.getString(R.string.default_request_error_timeout);
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                !e.getLocalizedMessage().equals("") &&
                e.getLocalizedMessage().length() <= 3) {
            int code = Integer.parseInt(e.getLocalizedMessage());
            switch (code) {
                case ResponseStatus.SC_REQUEST_TIMEOUT:
                    Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO));
                    return
                            context.getString(R.string.default_request_error_timeout);
                case ResponseStatus.SC_GATEWAY_TIMEOUT:
                    Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO));
                    return
                            context.getString(R.string.default_request_error_timeout);
                case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                    Log.d(TAG, getErrorInfo(code, SERVER_INFO));
                    return
                            context.getString(R.string.default_request_error_internal_server);
                case ResponseStatus.SC_FORBIDDEN:
                    Log.d(TAG, getErrorInfo(code, FORBIDDEN_INFO));
                    return context.getString(R.string.default_request_error_forbidden_auth);
                case ResponseStatus.SC_BAD_GATEWAY:
                    Log.d(TAG, getErrorInfo(code, BAD_REQUEST_INFO));
                    return
                            context.getString(R.string.default_request_error_bad_request);
                case ResponseStatus.SC_BAD_REQUEST:
                    Log.d(TAG, getErrorInfo(code, BAD_REQUEST_INFO));
                    return
                            context.getString(R.string.default_request_error_bad_request);
                default:
                    Log.d(TAG, getErrorInfo(code, UNKNOWN_INFO));
                    return
                            context.getString(R.string.default_request_error_unknown);
            }
        } else if (e instanceof ErrorMessageException
                && !TextUtils.isEmpty(e.getLocalizedMessage())) {
            return e.getLocalizedMessage();
        } else if (e instanceof ResponseV4ErrorException) {
            return ((ResponseV4ErrorException) e).getErrorList().get(0);
        } else if (e instanceof IOException) {
            return context.getString(R.string.default_request_error_internal_server);
        } else {
            return context.getString(R.string.default_request_error_unknown);
        }
    }


    public static String getErrorMessageWithErrorCode(Context context, Throwable e) {
        return getErrorMessage(e, context);
    }

    public static String getDefaultErrorCodeMessage(int errorCode, Context context) {
        return context.getString(R.string.default_request_error_unknown) + " (" + errorCode + ")";
    }
}
