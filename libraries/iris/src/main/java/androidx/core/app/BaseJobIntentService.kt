package androidx.core.app

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import timber.log.Timber

/**
 * This fix should be tracked from time to time as any change in the support libraries might break it.
 *
 * https://issuetracker.google.com/issues/63622293
 * Google should fix it!
 */
abstract class BaseJobIntentService : JobIntentService() {

    internal override fun dequeueWork(): GenericWorkItem? {
        try {
            return super.dequeueWork()
        } catch (e: SecurityException) {
            ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to "dequeueWork", "err" to e.toString()))
        }
        return null
    }
}