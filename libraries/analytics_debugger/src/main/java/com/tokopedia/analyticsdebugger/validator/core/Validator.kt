package com.tokopedia.analyticsdebugger.validator.core

import com.tokopedia.analyticsdebugger.database.GtmLogDB

data class Validator(
        val name: String,
        val data: Map<String, Any>,
        var status: Status = Status.PENDING,
        var matches: List<GtmLogUi> = emptyList()
)

enum class Status {
    PENDING, SUCCESS, FAILURE
}