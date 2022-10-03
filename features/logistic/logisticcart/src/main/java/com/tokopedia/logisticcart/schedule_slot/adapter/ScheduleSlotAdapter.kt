package com.tokopedia.logisticcart.schedule_slot.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.logisticcart.schedule_slot.uimodel.BaseScheduleSlotUiModel

class ScheduleSlotAdapter(private val factory: ScheduleSlotTypeFactory)
    : BaseListAdapter<Visitable<*>, ScheduleSlotTypeFactory>(factory) {

    private var selectedItem: BaseScheduleSlotUiModel<out Any>? = null

    fun setData(data: List<BaseScheduleSlotUiModel<out Any>>) {
        visitables?.clear()
        visitables?.addAll(data)
        notifyDataSetChanged()
    }

    fun selectItem(item: BaseScheduleSlotUiModel<out Any>) {
        selectedItem = item
        factory.listener.onClickTimeListener(item)
    }
}
