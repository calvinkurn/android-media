package com.tokopedia.logisticcart.scheduledelivery.view.adapter

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.BaseScheduleSlotUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.BottomSheetUiModel
import com.tokopedia.logisticcart.scheduledelivery.utils.DividerType

class ScheduleSlotAdapter(private val factory: ScheduleSlotTypeFactory)
    : BaseListAdapter<Visitable<*>, ScheduleSlotTypeFactory>(factory) {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<BaseScheduleSlotUiModel<out Any>>) {
        visitables?.clear()
        visitables?.addAll(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: BottomSheetUiModel) {
        visitables?.clear()
        visitables.add(data.date)
        visitables.add(data.availableTitle)
        val selectedDate = data.date.content.find { it.isSelected }
        selectedDate?.let {
            visitables.addAll(it.availableTime.apply {
                last().divider = DividerType.THICK
            })
            if (selectedDate.unavailableTime.isNotEmpty()) {
                visitables.add(data.unavailableTitle)
                visitables.addAll(selectedDate.unavailableTime)
            }
        }
        notifyDataSetChanged()
    }
}
