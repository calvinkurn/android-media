package com.tokopedia.groupchat.common.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.common.network.ErrorNetworkException;
import com.tokopedia.vote.network.VoteErrorException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author by nisie on 2/21/18.
 */

public class GroupChatErrorHandler {

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
            return formattedString(context.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown),
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
        } else if (e instanceof VoteErrorException
                && !TextUtils.isEmpty(e.getMessage())
                && !TextUtils.isEmpty(((VoteErrorException) e).getErrorCode())) {
            return formattedString(e.getMessage(),
                    ((VoteErrorException) e).getErrorCode(), withCode);
        } else if (e instanceof VoteErrorException
                && !TextUtils.isEmpty(e.getMessage())) {
            return formattedString(e.getMessage(),
                    GroupChatErrorCode.WS_ERROR, withCode);
        } else if (e instanceof ConnectException){
            return formattedString(context.getString(com.tokopedia.abstraction.R.string.msg_no_connection_trouble)
                    , getErrorCode(e), withCode);
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
