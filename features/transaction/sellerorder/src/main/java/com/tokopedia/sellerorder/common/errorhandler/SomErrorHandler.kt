package com.tokopedia.sellerorder.common.errorhandler

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.sellerorder.BuildConfig
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.exception.SomException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object SomErrorHandler {

    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(SomException(
                        message = exceptionMessage,
                        cause = throwable
                ))
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun getErrorMessage(throwable: Throwable, context: Context): String {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException) {
            context.getString(R.string.som_error_message_no_internet_connection)
        } else {
            throwable.message ?: context.getString(R.string.som_error_message_server_fault)
        }
    }
}