package com.tokopedia.journeydebugger.domain.model

class JourneyLogModel {
    var journey: String? = null
    var traces: String? = null

    val data: String
        get() = "Journey: " + journey +
            "\r\nTraces: " + traces
}
