package com.tokopedia.home_account

import android.text.TextUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.network.data.model.response.ResponseV4ErrorException
import timber.log.Timber

object HomeAccountErrorHandler {
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
        Timber.w("P2#ACCOUNT_HOME_NEW_ERROR#'Failed render';'${getExceptionMessage(t)}';'${errorCode}'")
        try {
            FirebaseCrashlytics.getInstance().recordException(exception)
        } catch (exception: Exception) {

        }
    }
}

class AccountHomeException(message: String, throwable: Throwable) : Exception(message, throwable)