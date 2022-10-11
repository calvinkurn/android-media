package com.tokopedia.analyticsdebugger.cassava

interface AnalyticsLogger {

    fun save(
        data: Map<String, Any>,
        name: String? = null,
        source: String
    )

    fun enableNotification(status: Boolean)

    fun isNotificationEnabled(): Boolean

}
