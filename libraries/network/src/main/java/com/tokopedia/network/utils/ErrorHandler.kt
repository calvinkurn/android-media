package com.tokopedia.network.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.R
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import com.tokopedia.network.utils.ExceptionDictionary.Companion.getErrorCodeSimple
import com.tokopedia.network.utils.ExceptionDictionary.Companion.getRandomString
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class ErrorHandler {

    companion object {
        const val ERROR_HANDLER = "ERROR_HANDLER"
        const val DEFAULT_ERROR_CODE = ""

        @JvmStatic
        fun getErrorMessage(context: Context?, e: Throwable?): String {
            e?.let {
                return getErrorMessage(context, e, Builder())
            }
            return ""
        }

        @JvmStatic
        fun getErrorMessagePair(
                context: Context?,
                e: Throwable,
                builder: Builder
        ): Pair<String?, String> {
            val errorMessageString = getErrorMessageString(context, e)
            val errorCode: String = getErrorCode(context, e)
            val errorIdentifier = getRandomString(4)

            if (builder.sendToScalyr) {
                sendToScalyr(errorIdentifier, builder.className, errorCode, Log.getStackTraceString(e))
            }

            if(errorCode == DEFAULT_ERROR_CODE){
                return Pair(errorMessageString, "$errorIdentifier")
            }
            return Pair(errorMessageString, "$errorCode-$errorIdentifier")
        }

        @JvmStatic
        fun getErrorMessage(
            context: Context?,
            e: Throwable,
            builder: Builder
        ): String {

            val errorMessageString = getErrorMessageString(context, e)

            val errorCode: String = getErrorCode(context, e)
            val errorIdentifier = getRandomString(4)

            if (builder.sendToScalyr) {
                sendToScalyr(errorIdentifier, builder.className, errorCode, Log.getStackTraceString(e))
            }
            if (!builder.errorCode) {
                return "$errorMessageString";
            }

            if(errorCode == DEFAULT_ERROR_CODE){
                return "$errorMessageString. Kode Error: ($errorIdentifier)"
            }

            return "$errorMessageString. Kode Error: ($errorCode-$errorIdentifier)"
        }

        private fun getErrorMessageString(context: Context?, e: Throwable?): String? {
            if (context == null || e == null) {
                return "Terjadi kesalahan. Ulangi beberapa saat lagi."
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
                e.message!!
            } else if (e is AkamaiErrorException) {
                e.message!!
            } else if (e is IOException) {
                context.getString(R.string.default_request_error_internal_server)
            } else {
                context.getString(R.string.default_request_error_unknown)
            }
        }

        private fun getErrorCode(context: Context?, e: Throwable): String {
            return if (e is MessageErrorException) {
                getErrorMessageHTTP(e)
            } else {
                getErrorCodeSimple(e)
            }
        }

        private fun getErrorMessageHTTP(e: Throwable): String {
            return if (e is MessageErrorException && !e.errorCode.isNullOrEmpty() && e.errorCode != "200") {
                e.errorCode
            } else {
                DEFAULT_ERROR_CODE
            }
        }

        private fun sendToScalyr(errorIdentifier: String, className: String, errorCode: String, stackTraceString: String?) {
            val mapParam = mapOf(
                    "identifier" to errorIdentifier,
                    "class" to className,
                    "error_code" to errorCode,
                    "stack_trace" to stackTraceString.orEmpty()
            )
            ServerLogger.log(Priority.P1, ERROR_HANDLER, mapParam as Map<String, String>)
        }
    }

    class Builder {
        var sendToScalyr = true
        var errorCode = true
        var className = ""

        fun sendToScalyr(value: Boolean): Builder {
            sendToScalyr = value
            return this
        }

        fun withErrorCode(value: Boolean): Builder {
            errorCode = value
            return this
        }

        fun className(value: String): Builder {
            className = value
            return this
        }

        fun build(): Builder {
            return this
        }

    }


}