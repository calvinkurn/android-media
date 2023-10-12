package com.tokopedia.topads.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.create.databinding.ViewholderItemListBinding
import com.tokopedia.topads.view.uimodel.ItemListUiModel
import com.tokopedia.topads.view.utils.ScheduleSlotListener
import com.tokopedia.topads.view.viewholder.ItemListViewHolder

class ListBottomSheetItemFactory(val listener: ScheduleSlotListener) :
    BaseAdapterTypeFactory(),
    ListBottomSheetItemViewHolder {

    override fun type(model: ItemListUiModel): Int = ItemListViewHolder.LAYOUT_RES

    override fun createViewHolder(
        parent: View,
        type: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ItemListViewHolder.LAYOUT_RES -> {
                val viewBinding = ViewholderItemListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                ItemListViewHolder(viewBinding, listener)
            }
            else -> super.createViewHolder(parent, type)
        }
    }
}


interface ListBottomSheetItemViewHolder {
    fun type(model: ItemListUiModel): Int
}
