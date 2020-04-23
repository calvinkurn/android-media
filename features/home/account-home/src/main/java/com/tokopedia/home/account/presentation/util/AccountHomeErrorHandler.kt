package com.tokopedia.home.account.presentation.util

import android.text.TextUtils
import com.crashlytics.android.Crashlytics
import com.tokopedia.home.account.BuildConfig
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import timber.log.Timber
import java.lang.Exception
import java.net.URLEncoder

object AccountHomeErrorHandler {
    @JvmStatic
    fun getExceptionMessage(t: Throwable): String {
        return try {
            if (t is ResponseV4ErrorException && !TextUtils.isEmpty(t.errorList.firstOrNull())) {
                t.errorList.firstOrNull() ?: t.localizedMessage
            } else if(t.localizedMessage != null){
                t.localizedMessage
            } else {
                t.toString()
            }
        }
        catch (e: Exception){
            ""
        }
    }

    @JvmStatic
    fun logExceptionToCrashlytics(t: Throwable, userId: String, email:String, errorCode:String) {

        val errorMessage = String.format(
                "\"Error account home.\",\"userId: %s\",\"userEmail: %s \",\"errorMessage: %s\",\"%s\"",
                userId,
                email,
                getExceptionMessage(t),
                errorCode)
        val exception = AccountHomeException(errorMessage, t)


        if (!BuildConfig.DEBUG) {
            try {
                Crashlytics.logException(exception)
            } catch (e: Exception) {
                Timber.w("P2#ACCOUNT_HOME#Failed renderC;$errorMessage $e")
            }
        } else {
            Timber.w("P2#ACCOUNT_HOME#Failed render;$errorMessage")
        }
    }
}