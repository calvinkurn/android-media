package com.tokopedia.journeydebugger.domain.model

class JourneyLogModel {
    var journey: String? = null

    val data: String
        get() = "Journey: " + journey
}
