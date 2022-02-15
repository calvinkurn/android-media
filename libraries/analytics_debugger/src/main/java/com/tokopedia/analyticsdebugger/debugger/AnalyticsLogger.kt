package com.tokopedia.analyticsdebugger.debugger

import com.tokopedia.analyticsdebugger.AnalyticsSource

interface AnalyticsLogger {

    fun save(
        data: Map<String, Any>,
        name: String? = null,
        @AnalyticsSource source: String = AnalyticsSource.OTHER
    )

    fun enableNotification(status: Boolean)

    fun isNotificationEnabled(): Boolean

}
