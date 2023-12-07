package com.tokopedia.promousage.util.extension

import android.content.Context
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promousage.R
import com.tokopedia.promousage.util.logger.PromoErrorException

internal fun Throwable.getErrorMessage(context: Context): String {
    var errorMessage = this.message
    if (this !is PromoErrorException && this !is AkamaiErrorException) {
        errorMessage = ErrorHandler.getErrorMessage(context, this)
    }
    if (errorMessage.isNullOrBlank()) {
        errorMessage = context.getString(R.string.promo_usage_global_error_promo)
    }
    return errorMessage
}
