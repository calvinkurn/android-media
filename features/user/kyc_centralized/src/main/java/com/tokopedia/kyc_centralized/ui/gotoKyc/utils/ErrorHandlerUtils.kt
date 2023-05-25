package com.tokopedia.kyc_centralized.ui.gotoKyc.utils

import android.content.Context
import com.tokopedia.kyc_centralized.R
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler

fun Throwable?.getGotoKycErrorMessage(context: Context): String {
    val message = context.getString(R.string.goto_kyc_error_from_be)
    return if (this == null || this is MessageErrorException) {
        message
    } else {
        val errorHandler = ErrorHandler.getErrorMessagePair(
            context = context,
            e = this,
            builder = ErrorHandler.Builder().build()
        )
        val errorCode = context.getString(R.string.error_code, errorHandler.second)
        message.plus(" $errorCode")
    }
}
