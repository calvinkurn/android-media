package com.tokopedia.analyticsdebugger.debugger.domain.model

class ApplinkLogModel {
    var applink: String? = null
    var traces: String? = null

    val data: String
        get() = "Applink: " + applink +
                "\r\nTraces: " + traces
}
