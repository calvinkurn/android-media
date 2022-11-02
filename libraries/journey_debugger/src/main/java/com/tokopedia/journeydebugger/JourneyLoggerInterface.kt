package com.tokopedia.journeydebugger

interface JourneyLoggerInterface {

    val isNotificationEnabled: Boolean

    fun save(journey: String)

    fun wipe()

    fun openActivity()

    fun enableNotification(status: Boolean)
}
