package com.tokopedia.sellerorder.requestpickup.data.model

sealed class SchedulePickupModelVisitable

data class ScheduleTime(
        val key: String = "",
        val start: String = "",
        val end : String = "",
        var isSelected: Boolean = false,
        var day: String = ""
)

data class Tomorrow(
        val keyTomorrow: String = "",
        val startTomorrow: String = "",
        val endTomorrow : String = ""
) : SchedulePickupModelVisitable()