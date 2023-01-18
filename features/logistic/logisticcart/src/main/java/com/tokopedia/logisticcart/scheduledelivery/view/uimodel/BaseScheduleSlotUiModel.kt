package com.tokopedia.logisticcart.scheduledelivery.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.logisticcart.scheduledelivery.view.adapter.ScheduleSlotTypeFactory
import com.tokopedia.logisticcart.scheduledelivery.utils.DividerType

interface  BaseScheduleSlotUiModel<T> :  Visitable<ScheduleSlotTypeFactory>  {
    val title: String
    val content: T
    val isEnabled: Boolean
    val divider: DividerType
}
