package com.tokopedia.review.common.util

import android.content.Context
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException

/**
 * @author by milhamj on 1/26/21.
 */

object ReviewErrorHandler {
    @JvmStatic
    fun getErrorMessage(context: Context, e: Throwable): String {
        val exceptionMessage: String? = (e as? ErrorMessageException)?.message
        return if (exceptionMessage != null && exceptionMessage.isNotBlank()) {
            exceptionMessage
        } else {
            ErrorHandler.getErrorMessage(context.applicationContext, e)
        }
    }
}