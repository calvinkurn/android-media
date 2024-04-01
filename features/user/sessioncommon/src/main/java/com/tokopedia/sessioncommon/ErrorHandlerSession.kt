package com.tokopedia.sessioncommon

import android.content.Context
import android.text.TextUtils
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.network.R as networkR

/**
 * @author by nisie on 10/2/18.
 */
object ErrorHandlerSession {
    fun getErrorMessage(context: Context?, e: Throwable?): String {
        return ErrorHandler.getErrorMessage(context, e)
    }

    fun getErrorMessage(listener: ErrorForbiddenListener, e: Throwable?, context: Context) {
        val forbiddenMessage = context.getString(R.string.default_request_error_forbidden_auth)
        val errorMessage = ErrorHandler.getErrorMessage(context, e)
        if (errorMessage == forbiddenMessage) {
            listener.onForbidden()
        } else {
            listener.onError(errorMessage)
        }
    }

    fun getDefaultErrorCodeMessage(errorCode: Int, context: Context): String {
        return (context.getString(networkR.string.default_request_error_unknown)
            + " (" + errorCode + ")")
    }

    fun getErrorMessage(e: Throwable, context: Context?, showErrorCode: Boolean): String {
        return if (e is MessageErrorException
            && !TextUtils.isEmpty(e.getLocalizedMessage())) {
            if (showErrorCode) formatString(e.getLocalizedMessage()
                ?: "", e.errorCode) else e.getLocalizedMessage()
        } else {
            ErrorHandler.getErrorMessage(context, e)
        }
    }

    private fun formatString(message: String, errorCode: String): String {
        return String.format("%s ( %s )", message, errorCode)
    }

    interface ErrorCode {
        companion object {
            const val UNKNOWN = 1000
            const val UNKNOWN_HOST_EXCEPTION = 1001
            const val SOCKET_TIMEOUT_EXCEPTION = 1002
            const val IO_EXCEPTION = 1003
            const val WS_ERROR = 1004
            const val UNSUPPORTED_FLOW = 1005
            const val EMPTY_ACCESS_TOKEN = 1123
            const val GOOGLE_FAILED_ACCESS_TOKEN = 1124
            const val WEBVIEW_ERROR = 1125
            const val EMPTY_EMAIL = 1126
        }
    }

    interface ErrorForbiddenListener {
        fun onForbidden()
        fun onError(errorMessage: String?)
    }
}
