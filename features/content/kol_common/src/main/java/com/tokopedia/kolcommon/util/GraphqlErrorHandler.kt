package com.tokopedia.kolcommon.util

import android.content.Context

import com.tokopedia.abstraction.common.utils.network.ErrorHandler

/**
 * @author by milhamj on 19/04/18.
 */

object GraphqlErrorHandler {

    @JvmStatic
    fun getErrorMessage(context: Context, e: Throwable): String {
        return if (e is GraphqlErrorException) {
            e.getLocalizedMessage()
        } else {
            ErrorHandler.getErrorMessage(context, e)
        }
    }

}
