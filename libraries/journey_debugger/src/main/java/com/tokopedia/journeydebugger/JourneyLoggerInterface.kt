package com.tokopedia.analyticsdebugger.debugger

interface JourneyLoggerInterface {

    val isNotificationEnabled: Boolean

    fun startTrace(applink: String)

    fun appendTrace(trace: String)

    fun save()

    fun wipe()

    fun openActivity()

    fun enableNotification(status: Boolean)
}
