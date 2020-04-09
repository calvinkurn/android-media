package com.tokopedia.analyticsdebugger.debugger

interface ApplinkLoggerInterface {

    val isNotificationEnabled: Boolean

    fun startTrace(applink: String)

    fun appendTrace(trace: String)

    fun save()

    fun wipe()

    fun openActivity()

    fun enableNotification(status: Boolean)
}
