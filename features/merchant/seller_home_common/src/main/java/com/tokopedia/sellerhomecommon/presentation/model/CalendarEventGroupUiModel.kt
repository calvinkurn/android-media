package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

data class CalendarEventGroupUiModel(
    val events: List<CalendarEventUiModel> = emptyList(),
    val autoScrollToHere: Boolean = false
)
