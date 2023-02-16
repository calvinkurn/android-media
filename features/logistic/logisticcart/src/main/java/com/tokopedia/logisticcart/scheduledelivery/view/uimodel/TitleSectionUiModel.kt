package com.tokopedia.logisticcart.scheduledelivery.view.uimodel

import com.tokopedia.logisticcart.scheduledelivery.view.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.scheduledelivery.utils.DividerType

data class TitleSectionUiModel(
    override val title: String = "",
    override val content: String = "",
    val icon: Int = NO_ICON,
) : BaseScheduleSlotUiModel<String> {
    override fun type(typeFactory: ScheduleSlotTypeFactory): Int {
        return typeFactory.type(this)
    }

    override val isEnabled: Boolean
        get() = true
    override val divider: DividerType
        get() = DividerType.NONE

    companion object {
        private val NO_ICON = -1
    }
}
