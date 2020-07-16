package com.tokopedia.home.account.presentation.util

import android.text.TextUtils
import com.crashlytics.android.Crashlytics
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.account.BuildConfig
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
                "userId='%s';email='%s';error_msg='%s';error_code='%s'",
                userId,
                email,
                getExceptionMessage(t),
                errorCode)
        val exception = AccountHomeException(errorMessage, t)

        Timber.w("P2#ACCOUNT_HOME_ERROR#'Failed render';$errorMessage;'$exception'")
        try {
            Crashlytics.logException(exception)
        } catch (exception: Exception) {

        }
    }
}