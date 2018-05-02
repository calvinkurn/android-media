package com.tokopedia.groupchat.common.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.common.network.ErrorNetworkException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author by nisie on 2/21/18.
 */

public class GroupChatErrorHandler {
    private static final int ERR_INVALID_INITIALIZATION = 800100;
    private static final int ERR_CONNECTION_REQUIRED = 800101;
    private static final int ERR_INVALID_PARAMETER = 800110;
    private static final int ERR_NETWORK = 800120;
    private static final int ERR_NETWORK_ROUTING_ERROR = 800121;
    private static final int ERR_MALFORMED_DATA = 800130;
    private static final int ERR_MALFORMED_ERROR_DATA = 800140;
    private static final int ERR_WRONG_CHANNEL_TYPE = 800150;
    private static final int ERR_MARK_AS_READ_RATE_LIMIT_EXCEEDED = 800160;
    private static final int ERR_QUERY_IN_PROGRESS = 800170;
    private static final int ERR_ACK_TIMEOUT = 800180;
    private static final int ERR_LOGIN_TIMEOUT = 800190;
    private static final int ERR_WEBSOCKET_CONNECTION_CLOSED = 800200;
    private static final int ERR_WEBSOCKET_CONNECTION_FAILED = 800210;
    private static final int ERR_REQUEST_FAILED = 800220;

    public static final int CHANNEL_NOT_FOUND = 400201;
    public static final int USER_IS_BANNED = 900100;
    public static final int WORD_IS_BANNED = 900060;


    public static String getSendBirdErrorMessage(Context context, SendBirdException e, boolean withCode) {
        switch (e.getCode()) {
            case ERR_INVALID_INITIALIZATION:
            case ERR_WRONG_CHANNEL_TYPE:
            case ERR_QUERY_IN_PROGRESS:
            case ERR_INVALID_PARAMETER:
            case ERR_MARK_AS_READ_RATE_LIMIT_EXCEEDED:
                return formattedString(context.getString(R.string
                        .sendbird_error_retry), e.getCode(), withCode);
            case ERR_CONNECTION_REQUIRED:
            case ERR_NETWORK:
            case ERR_WEBSOCKET_CONNECTION_CLOSED:
            case ERR_REQUEST_FAILED:
                return formattedString(context.getString(R.string
                        .sendbird_error_network), e.getCode(), withCode);
            case ERR_NETWORK_ROUTING_ERROR:
            case ERR_MALFORMED_DATA:
            case ERR_MALFORMED_ERROR_DATA:
            case ERR_WEBSOCKET_CONNECTION_FAILED:
                return formattedString(context.getString(R.string
                        .sendbird_error_server), e.getCode(), withCode);
            case ERR_ACK_TIMEOUT:
            case ERR_LOGIN_TIMEOUT:
                return formattedString(context.getString(R.string
                        .default_request_error_timeout), e.getCode(), withCode);
            case CHANNEL_NOT_FOUND:
                return formattedString(context.getString(R.string.channel_not_found_message), e
                        .getCode(), withCode);
            case USER_IS_BANNED:
                return formattedString(context.getString(R.string.user_is_banned), e.getCode(), withCode);
            case WORD_IS_BANNED:
                return formattedString(context.getString(R.string.word_is_blocked), e.getCode(),
                        withCode);
            default:
                return formattedString(context.getString(R.string.default_sendbird_error),
                        GroupChatErrorCode.UNKNOWN, withCode);
        }
    }

    private static String formattedString(String errorMessage, String code, boolean withCode) {
        if (withCode)
            return errorMessage + " (" + code + ")";
        else
            return errorMessage;
    }

    private static String formattedString(String errorMessage, int code, boolean withCode) {
        return formattedString(errorMessage, String.valueOf(code), withCode);
    }

    public static String getErrorMessage(Context context, Throwable e, boolean withCode) {
        if (e instanceof NullPointerException) {
            return formattedString(context.getString(R.string.default_request_error_unknown),
                    GroupChatErrorCode.MALFORMED_DATA, withCode);
        } else if (e instanceof JsonSyntaxException
                && !TextUtils.isEmpty(e.getMessage())) {
            return formattedString(e.getMessage(),
                    GroupChatErrorCode.MALFORMED_DATA, withCode);
        } else if (e instanceof ErrorNetworkException
                && !TextUtils.isEmpty(e.getMessage())
                && !TextUtils.isEmpty(((ErrorNetworkException) e).getErrorCode())) {
            return formattedString(e.getMessage(),
                    ((ErrorNetworkException) e).getErrorCode(), withCode);
        } else if (e instanceof ErrorNetworkException
                && !TextUtils.isEmpty(e.getMessage())) {
            return formattedString(e.getMessage(),
                    GroupChatErrorCode.WS_ERROR, withCode);
        } else {
            return formattedString(ErrorHandler.getErrorMessage(context, e), getErrorCode(e),
                    withCode);
        }
    }

    private static int getErrorCode(Throwable e) {
        if (e instanceof UnknownHostException) {
            return GroupChatErrorCode.UNKNOWN_HOST_EXCEPTION;
        } else if (e instanceof SocketTimeoutException) {
            return GroupChatErrorCode.SOCKET_TIMEOUT_EXCEPTION;
        } else if (e instanceof RuntimeException &&
                e.getLocalizedMessage() != null &&
                !e.getLocalizedMessage().equals("") &&
                e.getLocalizedMessage().length() <= 3) {
            return Integer.parseInt(e.getLocalizedMessage());
        } else if (e instanceof IOException) {
            return GroupChatErrorCode.IO_EXCEPTION;
        } else {
            return GroupChatErrorCode.UNKNOWN;
        }
    }
}
