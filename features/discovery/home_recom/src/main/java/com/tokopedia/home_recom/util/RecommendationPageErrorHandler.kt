package com.tokopedia.home_recom.util

import android.content.Context
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.common.utils.network.ErrorHandler

/**
 * Created by devarafikry on 29/05/19.
 */
object RecommendationPageErrorHandler {

    fun getErrorMessage(context: Context, e: Throwable?): String {
        return if (e is com.tokopedia.network.exception.MessageErrorException && e.message?.isNotEmpty() == true) {
            e.message ?: context.getString(R.string.default_request_error_unknown)
        } else {
            ErrorHandler.getErrorMessage(context, e);
        }
    }
}