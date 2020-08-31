package com.tokopedia.product.detail.view.util

import android.content.Context
import com.tokopedia.network.utils.ErrorHandler

/**
 * Created by hendry on 28/03/19.
 */
object ProductDetailErrorHandler {

    fun getErrorMessage(context: Context, e: Throwable?): String = ErrorHandler.getErrorMessage(context, e);

}