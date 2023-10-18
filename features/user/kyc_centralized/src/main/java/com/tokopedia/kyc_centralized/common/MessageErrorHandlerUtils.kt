package com.tokopedia.kyc_centralized.common

import android.content.Context
import android.text.TextUtils
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler

fun Throwable.getMessage(context: Context?): String {
    return if (this is MessageErrorException && !TextUtils.isEmpty(this.message)) {
        this.message.orEmpty()
    } else {
        ErrorHandler.getErrorMessage(context, this)
    }
}
