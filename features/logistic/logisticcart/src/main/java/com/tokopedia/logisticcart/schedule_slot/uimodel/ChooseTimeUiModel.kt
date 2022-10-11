package com.tokopedia.logisticcart.schedule_slot.uimodel

import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.schedule_slot.utils.DividerType

data class ChooseTimeUiModel(
    override val title: String = "",
    override val content: String = "",
    override var isEnabled: Boolean = true,
    override var divider: DividerType = DividerType.THIN,
    var isSelected: Boolean = false,
    val note: String = "",
    val timeId: Long = 0L,
    val dateId: String = ""
)
    : BaseScheduleSlotUiModel<String> {
    override fun type(typeFactory: ScheduleSlotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
