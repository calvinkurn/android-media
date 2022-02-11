package com.tokopedia.analyticsdebugger.debugger

import com.tokopedia.analyticsdebugger.AnalyticsSource

interface AnalyticsLogger {

    fun save(name: String, data: Map<String, Any>, @AnalyticsSource source:String)

    fun saveError(errorData: String)

    fun enableNotification(status: Boolean)

}
