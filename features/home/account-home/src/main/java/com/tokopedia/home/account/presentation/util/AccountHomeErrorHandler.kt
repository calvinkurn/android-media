package com.tokopedia.home.account.presentation.util

import android.text.TextUtils
import com.crashlytics.android.Crashlytics
import com.tokopedia.home.account.BuildConfig
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import java.net.URLEncoder

object AccountHomeErrorHandler {
    @JvmStatic
    fun getExceptionMessage(t: Throwable): String {
        return if (t is ResponseV4ErrorException && !TextUtils.isEmpty(t.errorList.firstOrNull())) {
            t.errorList.firstOrNull() ?: t.localizedMessage
        } else {
            t.localizedMessage
        }
    }

    @JvmStatic
    fun logExceptionToCrashlytics(t: Throwable, userId: String, email:String, errorCode:String) {
        if (!BuildConfig.DEBUG) {
            val errorMessage = String.format(
                    "\"Error account home.\",\"userId: %s\",\"userEmail: %s \",\"errorMessage: %s\",\"%s\"",
                    userId,
                    email,
                    AccountHomeErrorHandler.getExceptionMessage(t),
                    errorCode)
            val exception = AccountHomeException(errorMessage, t)

            try {
                Crashlytics.logException(exception)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }
}