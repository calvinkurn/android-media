package com.tokopedia.logisticcart.scheduledelivery.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.logisticcart.databinding.ViewholderChooseDateBinding
import com.tokopedia.logisticcart.databinding.ViewholderChooseTimeBinding
import com.tokopedia.logisticcart.databinding.ViewholderTitleSectionBinding
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ChooseDateUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ChooseTimeUiModel
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ScheduleSlotTypeViewHolder
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.TitleSectionUiModel
import com.tokopedia.logisticcart.scheduledelivery.utils.ScheduleSlotListener
import com.tokopedia.logisticcart.scheduledelivery.view.viewholder.ChooseDateViewHolder
import com.tokopedia.logisticcart.scheduledelivery.view.viewholder.ChooseTimeViewHolder
import com.tokopedia.logisticcart.scheduledelivery.view.viewholder.TitleSectionViewHolder

class ScheduleSlotTypeFactory(val listener: ScheduleSlotListener) : BaseAdapterTypeFactory(),
    ScheduleSlotTypeViewHolder {

    override fun type(model: ChooseDateUiModel): Int = ChooseDateViewHolder.LAYOUT_RES
    override fun type(model: ChooseTimeUiModel): Int = ChooseTimeViewHolder.LAYOUT_RES
    override fun type(model: TitleSectionUiModel): Int = TitleSectionViewHolder.LAYOUT_RES

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ChooseDateViewHolder.LAYOUT_RES -> {
                val viewBinding = ViewholderChooseDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                ChooseDateViewHolder(viewBinding, listener)
            }
            ChooseTimeViewHolder.LAYOUT_RES -> {
                val viewBinding = ViewholderChooseTimeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                ChooseTimeViewHolder(viewBinding, listener)
            }
            TitleSectionViewHolder.LAYOUT_RES -> {
                val viewBinding = ViewholderTitleSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                TitleSectionViewHolder(viewBinding, listener)
            }
            else -> super.createViewHolder(parent, type)

        }
    }
}
