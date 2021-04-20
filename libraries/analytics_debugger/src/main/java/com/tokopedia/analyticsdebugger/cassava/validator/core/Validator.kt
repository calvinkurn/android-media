package com.tokopedia.analyticsdebugger.cassava.validator.core

data class Validator(
        val data: Map<String, Any>,
        var status: Status = Status.PENDING,
        var matches: List<GtmLogUi> = emptyList(),
        val name: String = getAnalyticsName(data)
)

fun getAnalyticsName(item: Map<String, Any>): String = when {
    item.containsKey("eventAction") -> item["eventAction"] as String
    item.containsKey("event") -> item["event"] as String
    else -> item.keys.find { key -> item[key] is String }
            ?.let { item[it] as String }.orEmpty()
}

enum class Status {
    PENDING, SUCCESS, FAILURE
}