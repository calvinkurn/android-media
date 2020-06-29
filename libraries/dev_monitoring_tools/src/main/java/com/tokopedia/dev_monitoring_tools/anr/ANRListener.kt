package com.tokopedia.dev_monitoring_tools.anr

import android.util.Log
import com.github.anrwatchdog.ANRError
import com.github.anrwatchdog.ANRWatchDog
import com.tokopedia.dev_monitoring_tools.userjourney.UserJourney
import timber.log.Timber

class ANRListener(private val anrIgnoreList: List<String>, private val userJourneySize: Int = 5) : ANRWatchDog.ANRListener {

    override fun onAppNotResponding(anrError: ANRError?) {
        val error = Log.getStackTraceString(anrError)
        val match = anrIgnoreList.filter { error.contains(it, ignoreCase = true) }
        if (match.isEmpty()) {
            Timber.w("P1#DEV_ANR#${UserJourney.getLastActivity()};journey='${UserJourney.getReadableJourneyActivity(userJourneySize)}';anr='${Log.getStackTraceString(anrError)}'")
        }
    }
}