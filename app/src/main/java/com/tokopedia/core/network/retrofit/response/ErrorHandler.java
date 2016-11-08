package com.tokopedia.core.network.retrofit.response;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Angga.Prasetiyo on 01/12/2015.
 */
public class ErrorHandler {
    private static final String TAG = ErrorHandler.class.getSimpleName();
    private static final String SERVER_INFO = "Network Server Error";
    private static final String FORBIDDEN_INFO = "Network Forbidden";
    private static final String BAD_REQUEST_INFO = "Network Bad Request";
    private static final String UNKNOWN_INFO = "Network Error";
    private static final String TIMEOUT_INFO = "Network Timeout";

    public ErrorHandler(@NonNull ErrorListener listener, int code) {
        switch (code) {
            case ResponseStatus.SC_REQUEST_TIMEOUT:
                Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO));
                listener.onTimeout();
                break;
            /*case ResponseStatus.SC_GATEWAY_TIMEOUT:
                Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO));
                listener.onTimeout();
                break;
            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                Log.d(TAG, getErrorInfo(code, SERVER_INFO));
                listener.onServerError();
                break;
            case ResponseStatus.SC_FORBIDDEN:
                Log.d(TAG, getErrorInfo(code, FORBIDDEN_INFO));
                listener.onForbidden();
                break;
            case ResponseStatus.SC_BAD_GATEWAY:
                Log.d(TAG, getErrorInfo(code, BAD_REQUEST_INFO));
                listener.onBadRequest();
                break;
            case ResponseStatus.SC_BAD_REQUEST:
                Log.d(TAG, getErrorInfo(code, BAD_REQUEST_INFO));
                listener.onBadRequest();
                break;
            default:
                Log.d(TAG, getErrorInfo(code, UNKNOWN_INFO));
                listener.onUnknown();
                break;*/
            case ResponseStatus.SC_FORBIDDEN:
                Log.d(TAG, getErrorInfo(code, FORBIDDEN_INFO));
                listener.onForbidden();
                break;
            default:
                Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO));
                listener.onTimeout();
                break;
        }
    }

    private static String getErrorInfo(int code, String msg) {
        return "Error " + String.valueOf(code) + " : " + msg;
    }
}
