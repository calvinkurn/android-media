package com.tokopedia.logisticcart.scheduledelivery.view.adapter

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.logisticcart.scheduledelivery.utils.DividerType
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.BaseScheduleSlotUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.BottomSheetUiModel

class ScheduleSlotAdapter(private val factory: ScheduleSlotTypeFactory) :
    BaseListAdapter<Visitable<*>, ScheduleSlotTypeFactory>(factory) {

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
        visitables.addAll(generateTimeSlotList(data))
        notifyDataSetChanged()
    }

    fun setTimeSlot(data: BottomSheetUiModel) {
        removePreviousTimeSlot()
        val positionStart = visitables.size
        val timeSlotList = generateTimeSlotList(data)
        visitables.addAll(timeSlotList)
        notifyItemRangeInserted(positionStart, timeSlotList.size)
    }

    private fun generateTimeSlotList(data: BottomSheetUiModel): List<Visitable<*>> {
        val timeSlotList = mutableListOf<Visitable<*>>()
        timeSlotList.add(data.availableTitle)
        val selectedDate = data.date.content.find { it.isSelected }
        selectedDate?.let {
            timeSlotList.addAll(
                it.availableTime.apply {
                    last().divider = DividerType.THICK
                }
            )
            if (selectedDate.unavailableTime.isNotEmpty()) {
                timeSlotList.add(data.unavailableTitle)
                timeSlotList.addAll(selectedDate.unavailableTime)
            }
        }
        return timeSlotList
    }

    private fun removePreviousTimeSlot() {
        val currentItemCount = visitables.size
        visitables = visitables.subList(DATE_VH_INDEX, DATE_VH_INDEX + 1)
        if (currentItemCount > DATE_VH_INDEX + 1) {
            notifyItemRangeRemoved(DATE_VH_INDEX + 1, currentItemCount - (DATE_VH_INDEX + 1))
        }
    }

    companion object {
        private const val DATE_VH_INDEX = 0
    }
}
