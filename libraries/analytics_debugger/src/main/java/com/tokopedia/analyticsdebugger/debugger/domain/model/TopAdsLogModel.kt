package com.tokopedia.analyticsdebugger.debugger.domain.model

import java.util.HashMap

class TopAdsLogModel() {
    var url: String = ""
    var eventType: String = ""
    var sourceName: String = ""

    val data: String
        get() = "Url: " + url +
                "\r\neventType: " + eventType +
                "\r\nsourceName: " + sourceName
}
