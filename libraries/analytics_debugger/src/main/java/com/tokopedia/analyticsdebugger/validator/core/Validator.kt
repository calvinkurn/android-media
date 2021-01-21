package com.tokopedia.analyticsdebugger.validator.core

data class Validator(
        val name: String,
        val data: Map<String, Any>,
        var status: Status = Status.PENDING,
        var matches: List<GtmLogUi> = emptyList()
)

enum class Status {
    PENDING, SUCCESS, FAILURE
}