package com.tokopedia.analyticsdebugger.validator

data class Validator(
        val name: String,
        val data: Map<String, Any>,
        var timeStamp: String = "",
        var status: Status = Status.PENDING
)

enum class Status {
    PENDING, SUCCESS, FAILURE
}