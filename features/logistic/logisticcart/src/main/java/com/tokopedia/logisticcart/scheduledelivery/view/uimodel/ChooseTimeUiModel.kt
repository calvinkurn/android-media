package com.tokopedia.logisticcart.scheduledelivery.view.uimodel

import com.tokopedia.logisticcart.scheduledelivery.utils.DividerType
import com.tokopedia.logisticcart.scheduledelivery.view.adapter.ScheduleSlotTypeFactory

data class ChooseTimeUiModel(
    override val title: String = "",
    override val content: String = "",
    override var isEnabled: Boolean = true,
    override var divider: DividerType = DividerType.THIN,
    var isSelected: Boolean = false,
    val note: String = "",
    val timeId: Long = 0L,
    val dateId: String = ""
) : BaseScheduleSlotUiModel<String> {
    override fun type(typeFactory: ScheduleSlotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
