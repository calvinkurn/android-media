package com.tokopedia.analyticsdebugger.validator.core

data class Validator(
        val data: Map<String, Any>,
        var status: Status = Status.PENDING,
        var matches: List<GtmLogUi> = emptyList(),
        val name: String = getAnalyticsName(data)
)

private fun getAnalyticsName(item: Map<String, Any>): String {
    return if (item.containsKey("eventAction")) item["eventAction"] as String
    else item.keys.find { key ->
        item[key] is String
    }?.let { item[it] as String }.orEmpty()
}

enum class Status {
    PENDING, SUCCESS, FAILURE
}