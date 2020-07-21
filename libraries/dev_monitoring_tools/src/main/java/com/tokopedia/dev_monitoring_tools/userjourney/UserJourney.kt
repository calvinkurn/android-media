package com.tokopedia.dev_monitoring_tools.userjourney

import android.os.Bundle

object UserJourney {

    private const val MAX_JOURNEY_ACTIVITY = 10
    private val userJourney = arrayListOf<Journey>()

    fun addJourneyActivity(activityName: String, bundle: Bundle) {
        userJourney.add(Journey(activityName, bundle))
        if (userJourney.size > MAX_JOURNEY_ACTIVITY) {
            userJourney.removeAt(0)
        }
    }

    fun getReadableJourneyActivity(maxPath: Int = 5): String {
        val tempJourney = userJourney.takeLast(maxPath)
        var paths = ""
        for ((i, journey) in tempJourney.withIndex()) {
            paths += "${journey.activityName}[${bundleBreakdown(journey.bundle)}];"
        }
        return paths.dropLast(1)
    }

    fun getLastActivity(): String {
        var lastActivity = ""
        if (userJourney.size > 0) {
            val lastJourney = userJourney.last()
            lastActivity = lastJourney.activityName
        }
        return lastActivity
    }

    private fun bundleBreakdown(bundle: Bundle): String {
        var result = ""
        for (key in bundle.keySet()) {
            val bundleValue = bundle[key]
            bundleValue?.let {
                result += "$key=$it|"
            }
        }
        return result.dropLast(1)
    }

    data class Journey(
        var activityName: String = "", var bundle: Bundle = Bundle()
    )
}