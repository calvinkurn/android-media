package androidx.core.app

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.loginregister.login.service.RegisterPushNotifService

/**
 * This fix should be tracked from time to time as any change in the support libraries might break it.
 *
 * https://issuetracker.google.com/issues/63622293
 * Google should fix it!
 */
abstract class BaseRegisterPushNotifService : JobIntentService() {

    internal override fun dequeueWork(): GenericWorkItem? {
        try {
            return super.dequeueWork()
        } catch (e: SecurityException) {
            recordLog("dequeueWork()","", e)
        }
        return null
    }

    companion object {
        private val ERROR_HEADER = "${RegisterPushNotifService::class.java.name} error on "

        fun recordLog(type: String, message: String, throwable: Throwable) {
            val logMessage = if (message.isEmpty()) type else "$type | $message"
            sendLogToCrashlytics(logMessage, throwable)
            ServerLogger.log(Priority.P2, RegisterPushNotifService::class.java.name, mapOf(
                "type" to type,
                "err" to throwable.toString())
            )
        }

        private fun sendLogToCrashlytics(message: String, throwable: Throwable) {
            try {
                val messageException = "$ERROR_HEADER $message : ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(RuntimeException(messageException, throwable))
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }
}