package com.tokopedia.network.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.R
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ExceptionDictionary.Companion.getErrorCodeSimple
import com.tokopedia.network.utils.ExceptionDictionary.Companion.getRandomString
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {

    fun getErrorMessage(context: Context, e: Throwable): String {
        return getErrorMessage(context, e, Builder())
    }

    fun getErrorMessage(
        context: Context,
        e: Throwable,
        builder: Builder
    ): String {
        val errorMessageString =
            getErrorMessageString(context, e)
        val errorCode: String = if (e is MessageErrorException) {
            //http
            getErrorMessageHTTP(context, e)
        } else {
            // native
            getErrorCodeSimple(e)
        }
        val errorIdentifier = getRandomString(4)
        val mapParam = mapOf(
            "identifier" to errorIdentifier,
            "class" to context.javaClass.name,
            "e" to Log.getStackTraceString(e)
        )
        if (builder.errorCode) {
            mapParam.plus("error_code" to errorCode)
        }
        if (builder.sendToScalyr) {
            LogManager.log(Priority.P2, "ERROR_HANDLER", mapParam)
        }
        return "$errorMessageString <$errorCode-$errorIdentifier>"
    }

    fun getErrorMessageString(context: Context?, e: Throwable?): String? {
        if (context == null || e == null) {
            return "Terjadi kesalahan. Ulangi beberapa saat lagi"
        }
        return if (e is ResponseV4ErrorException) {
            e.errorList[0]
        } else if (e is UnknownHostException) {
            context.getString(R.string.msg_no_connection)
        } else if (e is SocketTimeoutException) {
            context.getString(R.string.default_request_error_timeout)
        } else if (e is RuntimeException && e.getLocalizedMessage() != null &&
            e.getLocalizedMessage() != "" && e.getLocalizedMessage().length <= 3
        ) {
            try {
                when (e.getLocalizedMessage().toInt()) {
                    ResponseStatus.SC_REQUEST_TIMEOUT, ResponseStatus.SC_GATEWAY_TIMEOUT -> context.getString(
                        R.string.default_request_error_timeout
                    )
                    ResponseStatus.SC_INTERNAL_SERVER_ERROR -> context.getString(R.string.default_request_error_internal_server)
                    ResponseStatus.SC_FORBIDDEN -> context.getString(R.string.default_request_error_forbidden_auth)
                    ResponseStatus.SC_BAD_GATEWAY -> context.getString(R.string.default_request_error_bad_request)
                    ResponseStatus.SC_BAD_REQUEST -> context.getString(R.string.default_request_error_bad_request)
                    ResponseStatus.SC_UNAUTHORIZED -> context.getString(R.string.msg_expired_session_or_unauthorized)
                    else -> context.getString(R.string.default_request_error_unknown)
                }
            } catch (e1: NumberFormatException) {
                context.getString(R.string.default_request_error_unknown)
            }
        } else if (e is MessageErrorException && !TextUtils.isEmpty(e.message)) {
            e.message
        } else if (e is IOException) {
            context.getString(R.string.default_request_error_internal_server)
        } else {
            context.getString(R.string.default_request_error_unknown)
        }
    }

    fun getErrorMessageHTTP(context: Context?, e: Throwable): String {
        return if (e is MessageErrorException && !TextUtils.isEmpty(e.message)) {
            e.errorCode
        } else {
            "000"
        }
    }

    class Builder {
        var sendToScalyr = true
        var errorCode = true

        fun sendToScalyr(value: Boolean): Builder {
            sendToScalyr = value
            return this
        }

        fun withErrorCode(value: Boolean): Builder {
            errorCode = value
            return this
        }

        fun build(): Builder {
            return this
        }

    }
}