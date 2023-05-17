package com.tokopedia.buyerorderdetail.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl

class PartialOrderFulfillmentDiffUtilCallback(
    private val oldItems: List<Visitable<PartialOrderFulfillmentTypeFactoryImpl>>,
    private val newItems: List<Visitable<PartialOrderFulfillmentTypeFactoryImpl>>,
    private val typeFactory: PartialOrderFulfillmentTypeFactoryImpl
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition)?.type(typeFactory) == newItems.getOrNull(
            newItemPosition
        )?.type(typeFactory)
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition) == newItems.getOrNull(newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return Pair(oldItem, newItem)
    }
}
