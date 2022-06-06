package com.tokopedia.home_account

import android.text.TextUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.data.model.response.ResponseV4ErrorException

object AccountErrorHandler {
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
    fun logDataNull(source: String, t: Throwable) {
        val exception = AccountHomeException(t.message ?: "", t)
        ServerLogger.log(Priority.P2, "ACCOUNT_HOME_ERROR", mapOf("type" to "Failed parsing model",
                "source" to source, "exception" to exception.toString()))
    }
}

class AccountHomeException(message: String, throwable: Throwable) : Exception(message, throwable)