package com.tokopedia.settingbank.domain.model

import android.content.Context
import com.tokopedia.abstraction.R
import com.tokopedia.network.utils.ErrorHandler

object SettingBankErrorHandler {
    fun getErrorMessage(context: Context, e: Throwable?): String {
        return if (e is com.tokopedia.network.exception.MessageErrorException && e.message?.isNotEmpty() == true) {
            e.message ?: context.getString(R.string.default_request_error_unknown)
        } else {
            ErrorHandler.getErrorMessage(context, e)
        }
    }
}