package com.tokopedia.home.account.presentation.util

import android.text.TextUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import timber.log.Timber

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
                "userId='%s';error_msg='%s';error_code='%s'",
                userId,
                getExceptionMessage(t),
                errorCode)
        val exception = AccountHomeException(errorMessage, t)

        ServerLogger.log(Priority.P2, "ACCOUNT_HOME_ERROR",
                mapOf("type" to "Failed render", "exception" to getExceptionMessage(t), "errorCode" to errorCode))
        try {
            FirebaseCrashlytics.getInstance().recordException(exception)
        } catch (exception: Exception) {

        }
    }

    @JvmStatic
    fun logDataNull(source: String, t: Throwable) {
        val exception = AccountHomeException(t.message ?: "", t)

        ServerLogger.log(Priority.P2, "ACCOUNT_HOME_ERROR",
                mapOf("type" to "Failed parsing model", "source" to source, "exception" to exception.toString()))
        try {
            FirebaseCrashlytics.getInstance().recordException(exception)
        } catch (exception: Exception) {

        }
    }

    // add handler
}