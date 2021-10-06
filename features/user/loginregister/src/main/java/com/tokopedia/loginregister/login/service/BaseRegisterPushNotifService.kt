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
            logToCrashlytics("dequeueWork() - catch()", e)
            ServerLogger.log(Priority.P2, "RegisterPushNotifService", mapOf("type" to "dequeueWork", "err" to e.toString()))
        }
        return null
    }

    companion object {
        private val ERROR_HEADER = "${RegisterPushNotifService::class.java.name} error on "

        fun logToCrashlytics(message: String, throwable: Throwable) {
            try {
                val messageException = "$ERROR_HEADER $message : ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(RuntimeException(messageException, throwable))
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }
}