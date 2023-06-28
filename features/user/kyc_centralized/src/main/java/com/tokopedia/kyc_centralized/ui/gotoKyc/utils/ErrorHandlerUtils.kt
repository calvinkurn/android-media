package com.tokopedia.kyc_centralized.ui.gotoKyc.utils

import android.content.Context
import com.tokopedia.kyc_centralized.R
import com.tokopedia.network.utils.ErrorHandler

fun Throwable?.getGotoKycErrorMessage(context: Context): String {
    val message = context.getString(R.string.goto_kyc_error_from_be)
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
    return message.plus(" $errorCode")
}
