package com.tokopedia.logisticcart.schedule_slot.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticcart.schedule_slot.uimodel.BaseScheduleSlotUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseDateUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.ChooseTimeUiModel
import com.tokopedia.logisticcart.schedule_slot.uimodel.TitleSectionUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.DividerType

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

    fun setData(date: ChooseDateUiModel) {
        visitables?.clear()
        visitables.add(date)
        visitables.add(TitleSectionUiModel(
            title = "Jadwal tersedia",
            content = "Kuota gratis ongkirmu habis",
            icon = IconUnify.INFORMATION))
        val selectedDate = date.content.find { it.isSelected }
        selectedDate?.let {
            visitables.addAll(it.availableTime.apply {
                last().divider = DividerType.THICK
            })
            if (selectedDate.unavailableTime.isNotEmpty()) {
                visitables.add(TitleSectionUiModel(title = "Jadwal habis atau tidak tersedia"))
                visitables.addAll(selectedDate.unavailableTime)
            }
        }
        notifyDataSetChanged()
    }
}
