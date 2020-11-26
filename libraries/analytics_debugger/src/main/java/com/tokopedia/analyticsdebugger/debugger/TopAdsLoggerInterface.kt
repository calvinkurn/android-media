package com.tokopedia.analyticsdebugger.debugger

interface TopAdsLoggerInterface {

    val isNotificationEnabled: Boolean

    fun save(url: String,
             eventType: String,
             sourceName: String,
             productId: String,
             productName: String,
             imageUrl: String,
             componentName: String
    )

    fun openActivity()

    fun enableNotification(status: Boolean)
}
