package com.tokopedia.dev_monitoring_tools.toolargetool

import android.util.Log
import com.gu.toolargetool.Logger
import timber.log.Timber

class TooLargeToolLogger() : Logger {

    override fun log(msg: String) {
        if (!msg.isBlank()) {
            Timber.w("P1#DEV_TOO_LARGE#warning;$msg")
        }
    }

    override fun logException(e: Exception) {
        Timber.w("P1#DEV_TOO_LARGE#exception;err='%s'", Log.getStackTraceString(e))
    }
}