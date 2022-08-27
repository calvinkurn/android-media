package com.tokopedia.analyticsdebugger.cassava

interface AnalyticsLogger {

    fun save(
        data: Map<String, Any>,
        name: String? = null,
        @AnalyticsSource source: String = AnalyticsSource.OTHER
    )

    fun enableNotification(status: Boolean)

    fun isNotificationEnabled(): Boolean

}
