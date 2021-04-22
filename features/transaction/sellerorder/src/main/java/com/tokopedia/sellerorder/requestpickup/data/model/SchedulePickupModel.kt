package com.tokopedia.sellerorder.requestpickup.data.model

import com.google.gson.annotations.SerializedName

sealed class SchedulePickupModelVisitable

data class SchedulePickupModel(
        var today: List<Today> = listOf(),
        var tomorrow: List<Tomorrow> = listOf()
)

data class Today(
        val keyToday: String = "",
        val startToday: String = "",
        val endToday : String = ""
) : SchedulePickupModelVisitable()

data class Tomorrow(
        val keyTomorrow: String = "",
        val startTomorrow: String = "",
        val endTomorrow : String = ""
) : SchedulePickupModelVisitable()