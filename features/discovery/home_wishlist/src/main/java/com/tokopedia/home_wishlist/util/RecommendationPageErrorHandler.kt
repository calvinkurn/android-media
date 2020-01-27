package com.tokopedia.home_wishlist.util

import android.content.Context
import com.tokopedia.abstraction.R
import com.tokopedia.network.utils.ErrorHandler

/**
 * Created by devarafikry on 29/05/19.
 * Modified by Lukaskris
 * A object for handling error message
 */
object RecommendationPageErrorHandler {
    /**
     * This function for handling message from throwable to String
     * @param context the context for getErrorMessage
     * @param e the throwable from network
     * @return The string from throwable network to `easy message` String
     */
    fun getErrorMessage(context: Context, e: Throwable?): String {
        return if (e is com.tokopedia.network.exception.MessageErrorException && e.message?.isNotEmpty() == true) {
            e.message ?: context.getString(R.string.default_request_error_unknown)
        } else {
            ErrorHandler.getErrorMessage(context, e)
        }
    }
}