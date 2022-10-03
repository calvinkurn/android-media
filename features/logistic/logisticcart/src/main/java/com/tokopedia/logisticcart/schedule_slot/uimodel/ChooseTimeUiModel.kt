package com.tokopedia.logisticcart.schedule_slot.uimodel

import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.schedule_slot.utils.DividerType

data class ChooseTimeUiModel(
    override val title: String = "",
    override val content: String = "",
    override val isEnabled: Boolean = true,
    override val divider: DividerType = DividerType.THIN
)
    : BaseScheduleSlotUiModel<String> {
    override fun type(typeFactory: ScheduleSlotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
