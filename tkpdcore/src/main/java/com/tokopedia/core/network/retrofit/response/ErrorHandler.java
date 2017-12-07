package com.tokopedia.core.network.retrofit.response;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

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
    private static final String ERROR_MESSAGE = "message_error";

    public ErrorHandler(@NonNull ErrorListener listener, int code) {
        switch (code) {
            case ResponseStatus.SC_REQUEST_TIMEOUT:
                Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO));
                listener.onTimeout();
                break;
            /*case ResponseStatus.SC_GATEWAY_TIMEOUT:
                Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO);
                listener.onTimeout(;
                break;
            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                Log.d(TAG, getErrorInfo(code, SERVER_INFO);
                listener.onServerError(;

                break;
            case ResponseStatus.SC_FORBIDDEN:
                Log.d(TAG, getErrorInfo(code, FORBIDDEN_INFO);
                listener.onForbidden(;
                break;
            case ResponseStatus.SC_BAD_GATEWAY:
                Log.d(TAG, getErrorInfo(code, BAD_REQUEST_INFO);
                listener.onBadRequest(;
                break;
            case ResponseStatus.SC_BAD_REQUEST:
                Log.d(TAG, getErrorInfo(code, BAD_REQUEST_INFO);
                listener.onBadRequest(;
                break;
            default:
                Log.d(TAG, getErrorInfo(code, UNKNOWN_INFO);
                listener.onUnknown(;
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

    public static String getErrorMessage(Throwable e, final Context context) {
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
                    return
                            context.getString(R.string.default_request_error_forbidden_auth);
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
                && e.getLocalizedMessage() != null) {
            return e.getLocalizedMessage();
        } else {
            return context.getString(R.string.default_request_error_unknown);
        }
    }

    public static String getErrorMessage(Throwable e) {
        Context context = MainApplication.getAppContext();
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
                    return
                            context.getString(R.string.default_request_error_forbidden_auth);
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
                && e.getLocalizedMessage() != null) {
            return e.getLocalizedMessage();
        } else if (BuildConfig.DEBUG) {
            return e.getLocalizedMessage();
        } else {
            return context.getString(R.string.default_request_error_unknown);
        }
    }


    public static String getErrorMessage(Response<TkpdResponse> response) {

            JsonElement jsonElement = new JsonParser().parse(response.errorBody().toString());
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (hasErrorMessage(jsonObject)) {
                JsonArray jsonArray = jsonObject.getAsJsonArray(ERROR_MESSAGE);
                return getErrorMessageJoined(jsonArray);
            } else {
                return "";
            }

    }


    private static boolean hasErrorMessage(JsonObject jsonObject) {
        return jsonObject.has(ERROR_MESSAGE);
    }

    public static String getErrorMessageJoined(JsonArray errorMessages) {

        StringBuilder stringBuilder = new StringBuilder();
        if (errorMessages.size() != 0) {
            for (int i = 0, statusMessagesSize = errorMessages.size(); i < statusMessagesSize; i++) {
                String string = String.valueOf(errorMessages.get(i));
                stringBuilder.append(string);
                if (i != errorMessages.size() - 1
                        && !errorMessages.get(i).equals("")
                        && !errorMessages.get(i + 1).equals("")) {
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }
}
