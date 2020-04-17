package com.tokopedia.analyticsdebugger.validator.core

import com.tokopedia.analyticsdebugger.database.GtmLogDB

data class Validator(
        val name: String,
        val data: Map<String, Any>,
        var status: Status = Status.PENDING,
        var match: GtmLogDB? = null
)

enum class Status {
    PENDING, SUCCESS, FAILURE
}