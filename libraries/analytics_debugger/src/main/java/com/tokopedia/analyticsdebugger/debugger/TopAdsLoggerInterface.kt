package com.tokopedia.analyticsdebugger.debugger

interface TopAdsLoggerInterface {

    val isNotificationEnabled: Boolean

    fun save(url: String,
             eventType: String,
             sourceName: String)

    fun openActivity()

    fun enableNotification(status: Boolean)
}
