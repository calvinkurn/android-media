package com.tokopedia.kyc_centralized.ui.gotoKyc.utils

import android.content.Context
import com.tokopedia.kyc_centralized.R
import com.tokopedia.network.utils.ErrorHandler
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable?.getGotoKycErrorMessage(context: Context): String {
    val errorCode = if (this != null) {
        val errorHandler = ErrorHandler.getErrorMessagePair(
            context = context,
            e = this,
            builder = ErrorHandler.Builder().build()
        )
        context.getString(R.string.error_code, errorHandler.second)
    } else {
        ""
    }
    val message = when (this) {
        is UnknownHostException, is ConnectException -> {
            context.getString(R.string.goto_kyc_error_no_connection)
        }
        is SocketTimeoutException -> {
            context.getString(R.string.goto_kyc_error_timeout)
        }
        else -> {
            context.getString(R.string.goto_kyc_error_from_be)
        }
    }
    return message.plus(" $errorCode")
}
