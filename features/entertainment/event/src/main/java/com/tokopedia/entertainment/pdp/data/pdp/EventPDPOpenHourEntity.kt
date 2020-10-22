package com.tokopedia.entertainment.pdp.data.pdp

data class EventPDPOpenHourEntity(
      val list : List<OpenHour> = emptyList()
)

data class OpenHour(
        val day : String = "",
        val hour: String = ""
)