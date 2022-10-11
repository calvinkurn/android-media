package com.tokopedia.logisticcart.schedule_slot.uimodel

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticcart.schedule_slot.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.schedule_slot.utils.DividerType

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
