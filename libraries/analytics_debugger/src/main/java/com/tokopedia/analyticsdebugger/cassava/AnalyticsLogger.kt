package com.tokopedia.analyticsdebugger.cassava

import com.tokopedia.analyticsdebugger.cassava.AnalyticsSource

interface AnalyticsLogger {

    fun save(
        data: Map<String, Any>,
        name: String? = null,
        @AnalyticsSource source: String = AnalyticsSource.OTHER
    )

    fun enableNotification(status: Boolean)

    fun isNotificationEnabled(): Boolean

}
