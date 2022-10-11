package com.tokopedia.logisticcart.schedule_slot.utils

import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseTimeUiModel

interface ScheduleSlotListener {
    fun onClickInfoListener()
    fun onClickDateListener(data: ButtonDateUiModel)
    fun onClickTimeListener(data: ChooseTimeUiModel)
}
