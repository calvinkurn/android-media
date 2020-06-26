package com.tokopedia.dev_monitoring_tools.session

object UserJourney {

    private const val MAX_JOURNEY_ACTIVITY = 10
    private val journeyActivity = arrayListOf<String>()

    fun addJourneyActivity(activityName: String) {
        journeyActivity.add(activityName)
        if (journeyActivity.size > MAX_JOURNEY_ACTIVITY) {
            journeyActivity.removeAt(0)
        }
    }

    fun getReadableJourneyActivity(maxPath: Int = 5) : String {
        val tempJourney = journeyActivity.takeLast(maxPath)
        var paths = ""
        for ((i, path) in tempJourney.withIndex()) {
            paths += "$path;"
        }
        return paths.dropLast(1)
    }
}