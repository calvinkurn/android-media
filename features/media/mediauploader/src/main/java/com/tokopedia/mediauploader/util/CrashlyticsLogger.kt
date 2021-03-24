package com.tokopedia.mediauploader.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.File

object CrashlyticsLogger {

    @Suppress("ThrowableNotThrown")
    fun toCrashlytics(filePath: File? = null, message: String) {
        if (filePath != null && filePath.path.isNotEmpty()) {
            val logMessage = "Error upload image %s because %s".format(filePath.path, message)
            val exception = MediaUplaoderException(logMessage)

            logExceptionToCrashlytics(exception)
        }
    }

    private fun logExceptionToCrashlytics(throwable: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}