package com.tokopedia.logisticcart.schedule_slot.utils

import com.tokopedia.logisticcart.schedule_slot.uimodel.BaseScheduleSlotUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel

interface ScheduleSlotListener {
    fun onClickInfoListener()
    fun onClickDateListener(data: ButtonDateUiModel)
    fun onClickTimeListener(data: BaseScheduleSlotUiModel<out Any>)
}
