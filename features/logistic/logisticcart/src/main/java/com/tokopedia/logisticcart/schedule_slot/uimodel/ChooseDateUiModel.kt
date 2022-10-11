package com.tokopedia.logisticcart.schedule_slot.uimodel

import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.schedule_slot.utils.DividerType

data class ChooseDateUiModel(
    override val title: String = "",
    override val content: List<ButtonDateUiModel> = listOf(),
    override val isEnabled: Boolean = false,
    override val divider: DividerType = DividerType.NONE
)
    : BaseScheduleSlotUiModel<List<ButtonDateUiModel>> {
    override fun type(typeFactory: ScheduleSlotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
