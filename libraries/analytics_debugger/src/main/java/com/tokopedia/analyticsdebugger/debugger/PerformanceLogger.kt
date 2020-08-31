package com.tokopedia.analyticsdebugger.debugger

interface PerformanceLogger {

    val isNotificationEnabled: Boolean

    val isAutoLogFileEnabled: Boolean
    fun save(traceName: String,
             startTime: Long,
             endTime: Long,
             attributes: Map<String, String>,
             metrics: Map<String, Long>)

    fun wipe()

    fun openActivity()

    fun enableNotification(status: Boolean)

    fun enableAutoLogFile(status: Boolean)
}
