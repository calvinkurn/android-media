package com.tokopedia.core.network.retrofit.response;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tkpd.library.utils.network.MessageErrorException;
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
    private static final String ERROR_MESSAGE_TOKOCASH = "errors";

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
                && !TextUtils.isEmpty(e.getLocalizedMessage())) {
            return e.getLocalizedMessage();
        } else {
            return context.getString(R.string.default_request_error_unknown);
        }
    }

    public static String getErrorMessage(Throwable e) {
        Context context = MainApplication.getAppContext();
        if (BuildConfig.DEBUG) {
            return e.getLocalizedMessage();
        } else if (e instanceof UnknownHostException) {
            return context.getString(R.string.msg_no_connection) + " " +
                    context.getString(R.string.code_error) + ErrorCode.UNKNOWN_HOST_EXCEPTION;
        } else if (e instanceof SocketTimeoutException) {
            return context.getString(R.string.default_request_error_timeout) + " " +
                    context.getString(R.string.code_error) + ErrorCode.SOCKET_TIMEOUT_EXCEPTION;
        } else if (e instanceof IOException) {
            return context.getString(R.string.msg_no_connection) + " " +
                    context.getString(R.string.code_error) + ErrorCode.IO_EXCEPTION;
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                !e.getLocalizedMessage().equals("") &&
                e.getLocalizedMessage().length() <= 3) {
            int code = Integer.parseInt(e.getLocalizedMessage());
            switch (code) {
                case ResponseStatus.SC_REQUEST_TIMEOUT:
                    Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO));
                    return
                            context.getString(R.string.default_request_error_timeout) + " " +
                                    context.getString(R.string.code_error) + " " + code;
                case ResponseStatus.SC_GATEWAY_TIMEOUT:
                    Log.d(TAG, getErrorInfo(code, TIMEOUT_INFO));
                    return
                            context.getString(R.string.default_request_error_timeout) + " " +
                                    context.getString(R.string.code_error) + " " + code;
                case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                    Log.d(TAG, getErrorInfo(code, SERVER_INFO));
                    return
                            context.getString(R.string.default_request_error_internal_server) + " " +
                                    context.getString(R.string.code_error) + " " + code;
                case ResponseStatus.SC_FORBIDDEN:
                    Log.d(TAG, getErrorInfo(code, FORBIDDEN_INFO));
                    return
                            context.getString(R.string.default_request_error_forbidden_auth) + " " +
                                    context.getString(R.string.code_error) + " " + code;
                case ResponseStatus.SC_BAD_GATEWAY:
                    Log.d(TAG, getErrorInfo(code, BAD_REQUEST_INFO));
                    return
                            context.getString(R.string.default_request_error_bad_request) + " " +
                                    context.getString(R.string.code_error) + " " + code;
                case ResponseStatus.SC_BAD_REQUEST:
                    Log.d(TAG, getErrorInfo(code, BAD_REQUEST_INFO));
                    return
                            context.getString(R.string.default_request_error_bad_request) + " " +
                                    context.getString(R.string.code_error) + " " + code;
                default:
                    Log.d(TAG, getErrorInfo(code, UNKNOWN_INFO));
                    return
                            context.getString(R.string.default_request_error_unknown) + " " +
                                    context.getString(R.string.code_error) + " " + code;
            }
        } else if (e instanceof ErrorMessageException
                && !TextUtils.isEmpty(e.getLocalizedMessage())) {
            if (!e.getLocalizedMessage().contains(context.getString(R.string.code_error)))
                return e.getLocalizedMessage() + " " +
                        context.getString(R.string.code_error) + ErrorCode.WS_ERROR;
            else {
                return e.getLocalizedMessage();
            }
        } else if (e instanceof MessageErrorException) {
            return e.getLocalizedMessage();
        } else {
            return context.getString(R.string.default_request_error_unknown) + " " +
                    context.getString(R.string.code_error) + ErrorCode.UNKNOWN;
        }

    }

    public static String getErrorMessage(Response response) {
        try {
            JSONObject jsonObject = new JSONObject(response.errorBody().string());

            if (hasErrorMessage(jsonObject)) {
                JSONArray jsonArray = jsonObject.getJSONArray(ERROR_MESSAGE);
                return getErrorMessageJoined(jsonArray);
            } else {
                return "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean hasErrorMessage(JSONObject jsonObject) {
        return jsonObject.has(ERROR_MESSAGE);
    }

    public static String getErrorMessageJoined(JSONArray errorMessages) {
        try {

            StringBuilder stringBuilder = new StringBuilder();
            if (errorMessages.length() != 0) {
                for (int i = 0, statusMessagesSize = errorMessages.length(); i < statusMessagesSize; i++) {
                    String string = null;
                    string = errorMessages.getString(i);
                    stringBuilder.append(string);
                    if (i != errorMessages.length() - 1
                            && !errorMessages.get(i).equals("")
                            && !errorMessages.get(i + 1).equals("")) {
                        stringBuilder.append("\n");
                    }
                }
            }
            return stringBuilder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDefaultErrorCodeMessage(int errorCode) {
        return MainApplication.getAppContext().getString(R.string.default_request_error_unknown)
                + " " + MainApplication.getAppContext().getString(R.string.code_error)
                + " " + errorCode;
    }

    public static String getErrorMessageTokoCash(Response<TkpdDigitalResponse> response) {
        try {
            JSONObject jsonObject = new JSONObject(response.errorBody().string());

            if (hasErrorMessageTokoCash(jsonObject)) {
                JSONArray jsonArray = jsonObject.getJSONArray(ERROR_MESSAGE_TOKOCASH);
                return getErrorMessageJoined(jsonArray);
            } else {
                return "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean hasErrorMessageTokoCash(JSONObject jsonObject) {
        return jsonObject.has(ERROR_MESSAGE_TOKOCASH);
    }
}
