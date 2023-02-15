package com.tokopedia.logisticcart.scheduledelivery.utils

import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ChooseTimeUiModel

interface ScheduleSlotListener {
    fun onClickInfoListener()
    fun onClickDateListener(data: ButtonDateUiModel)
    fun onClickTimeListener(data: ChooseTimeUiModel)
}
