package com.tokopedia.dev_monitoring_tools.anr

import android.util.Log
import com.github.anrwatchdog.ANRError
import com.github.anrwatchdog.ANRWatchDog
import com.tokopedia.dev_monitoring_tools.userjourney.UserJourney
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import timber.log.Timber

class ANRListener(private val anrIgnoreList: List<String>, private val userJourneySize: Int = 5) : ANRWatchDog.ANRListener {

    override fun onAppNotResponding(anrError: ANRError?) {
        val error = Log.getStackTraceString(anrError)
        val match = anrIgnoreList.filter { error.contains(it, ignoreCase = true) }
        if (match.isEmpty()) {
            ServerLogger.log(Priority.P1, "DEV_ANR", mapOf(
                    "type" to UserJourney.getLastActivity(),
                    "journey" to UserJourney.getReadableJourneyActivity(userJourneySize),
                    "anr" to Log.getStackTraceString(anrError)
            ))
        }
    }
}