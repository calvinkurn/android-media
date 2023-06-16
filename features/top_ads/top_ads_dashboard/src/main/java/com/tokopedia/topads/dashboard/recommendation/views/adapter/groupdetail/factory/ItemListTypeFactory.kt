package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.databinding.TopadsInsightCentreInsightSelctionItemBinding
import com.tokopedia.topads.dashboard.recommendation.utils.OnItemSelectChangeListener
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ItemListUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder.ItemListViewHolder

class ItemListTypeFactory(val listener: OnItemSelectChangeListener) :
    BaseAdapterTypeFactory(),
    ItemListTypeViewHolder {

    override fun type(model: ItemListUiModel): Int = ItemListViewHolder.LAYOUT_RES

    override fun createViewHolder(
        parent: View,
        type: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ItemListViewHolder.LAYOUT_RES -> {
                val viewBinding = TopadsInsightCentreInsightSelctionItemBinding.inflate(
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


interface ItemListTypeViewHolder {
    fun type(model: ItemListUiModel): Int
}
