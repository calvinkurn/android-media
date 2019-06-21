package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.common.utils.network.ErrorHandler

/**
 * Created by hendry on 28/03/19.
 */
object ProductDetailErrorHandler {

    fun getErrorMessage(context: Context, e: Throwable?): String {
        return if (e is com.tokopedia.network.exception.MessageErrorException && e.message?.isNotEmpty() == true) {
            e.message ?: context.getString(R.string.default_request_error_unknown)
        } else {
            ErrorHandler.getErrorMessage(context, e);
        }
    }
}