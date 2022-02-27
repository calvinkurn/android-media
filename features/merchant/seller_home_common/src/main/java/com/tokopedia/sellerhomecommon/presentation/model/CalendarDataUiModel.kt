package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

data class CalendarDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    val eventGroups: List<CalendarEventGroupUiModel> = emptyList()
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return eventGroups.isEmpty()
    }
}
