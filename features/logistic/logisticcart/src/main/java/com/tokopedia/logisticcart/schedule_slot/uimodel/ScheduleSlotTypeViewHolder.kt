package com.tokopedia.logisticcart.schedule_slot.uimodel

interface ScheduleSlotTypeViewHolder {
    fun type(model: ChooseDateUiModel): Int
    fun type(model: ChooseTimeUiModel): Int
}
