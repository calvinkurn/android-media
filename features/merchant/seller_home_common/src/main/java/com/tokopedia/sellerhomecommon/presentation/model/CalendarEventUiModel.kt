package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

data class CalendarEventUiModel(
    val eventName: String = "",
    val description: String = "",
    val label: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val appLink: String = ""
)
