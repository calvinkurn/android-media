package com.tokopedia.kyc_centralized.ui.gotoKyc.utils

import android.content.Context
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable?.getGotoKycErrorMessage(context: Context): String {
    var errorCode = ""
    val message = when (this) {
        is UnknownHostException, is ConnectException -> {
            context.getString(R.string.goto_kyc_error_no_connection)
        }
        is SocketTimeoutException -> {
            context.getString(R.string.goto_kyc_error_timeout)
        }
        else -> {
            var messageError = context.getString(R.string.goto_kyc_error_from_be)
            if (this != null) {
                if (this is MessageErrorException && this.errorCode == KYCConstant.KEY_KNOW_ERROR_CODE) {
                    // if errorCode is KYCConstant.KEY_KNOW_ERROR_CODE that mean the message already have valid errorCode
                    // link of errorCode: https://docs.google.com/spreadsheets/d/1pk5DOc1wAxUMUNNxeRAqZxPFGUHbLwBkmLijNI6cH8g/edit?usp=sharing
                    messageError = this.message.toString()
                } else {
                    val errorHandler = ErrorHandler.getErrorMessagePair(
                        context = context,
                        e = this,
                        builder = ErrorHandler.Builder().build()
                    )
                    errorCode = context.getString(R.string.error_code, errorHandler.second)
                }
            }

            messageError
        }
    }
    return message.plus(" $errorCode")
}
