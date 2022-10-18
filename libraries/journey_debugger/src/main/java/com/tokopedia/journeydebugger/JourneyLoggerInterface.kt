package com.tokopedia.journeydebugger

interface JourneyLoggerInterface {

    val isNotificationEnabled: Boolean

    fun startTrace(journey: String)

    fun appendTrace(trace: String)

    fun save()

    fun wipe()

    fun openActivity()

    fun enableNotification(status: Boolean)
}
