package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl

class OrderTrackingDiffUtilCallback(
    private val oldItems: List<Visitable<OrderTrackingAdapterTypeFactoryImpl>>,
    private val newItems: List<Visitable<OrderTrackingAdapterTypeFactoryImpl>>,
    private val orderTrackingAdapterTypeFactory: OrderTrackingAdapterTypeFactoryImpl
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition)?.type(orderTrackingAdapterTypeFactory) == newItems.getOrNull(newItemPosition)?.type(orderTrackingAdapterTypeFactory)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition) == newItems.getOrNull(newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return Pair(oldItem, newItem)
    }
}