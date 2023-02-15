package com.tokopedia.logisticcart.scheduledelivery.view.uimodel

interface ScheduleSlotTypeViewHolder {
    fun type(model: ChooseDateUiModel): Int
    fun type(model: ChooseTimeUiModel): Int
    fun type(model: TitleSectionUiModel): Int
}
