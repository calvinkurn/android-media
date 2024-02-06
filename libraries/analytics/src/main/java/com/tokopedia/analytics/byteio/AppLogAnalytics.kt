package com.tokopedia.analytics.byteio

import com.bytedance.applog.AppLog
import com.tokopedia.analyticsdebugger.cassava.Cassava
import org.json.JSONObject

object AppLogAnalytics {

    fun sendEnterPage() {}

    fun sendImpression() {}

    fun sendClick() {}

    fun sendStay() {}

    fun send(event: String, params: JSONObject) {
        Cassava.save(params, event, "ByteIO")
        AppLog.onEventV3(event, params)
    }

}
