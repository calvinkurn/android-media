package com.tokopedia.sellerorder.list.presentation.adapter.typefactories

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderEmptyViewHolder
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderViewHolder
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel

class SomListAdapterTypeFactory(private val orderItemListener: SomListOrderViewHolder.SomListOrderItemListener) : BaseAdapterTypeFactory() {
    fun type(orderUiModel: SomListOrderUiModel): Int {
        return SomListOrderViewHolder.LAYOUT
    }

    fun type(somListEmptyStateUiModel: SomListEmptyStateUiModel): Int {
        return SomListOrderEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SomListOrderViewHolder.LAYOUT -> SomListOrderViewHolder(parent, orderItemListener)
            SomListOrderEmptyViewHolder.LAYOUT -> SomListOrderEmptyViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}