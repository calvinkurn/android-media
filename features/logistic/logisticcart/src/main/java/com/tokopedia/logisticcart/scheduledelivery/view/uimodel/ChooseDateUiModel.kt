package com.tokopedia.logisticcart.scheduledelivery.view.uimodel

import com.tokopedia.logisticcart.scheduledelivery.utils.DividerType
import com.tokopedia.logisticcart.scheduledelivery.view.adapter.ScheduleSlotTypeFactory

data class ChooseDateUiModel(
    override val title: String = "",
    override val content: List<ButtonDateUiModel> = listOf(),
    override val isEnabled: Boolean = false,
    override val divider: DividerType = DividerType.NONE
) : BaseScheduleSlotUiModel<List<ButtonDateUiModel>> {
    override fun type(typeFactory: ScheduleSlotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
