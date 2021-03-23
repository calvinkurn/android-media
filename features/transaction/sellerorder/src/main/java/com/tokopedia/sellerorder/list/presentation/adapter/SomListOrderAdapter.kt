package com.tokopedia.sellerorder.list.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerorder.list.presentation.adapter.diffutilcallbacks.SomListOrderDiffUtilCallback
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel

class SomListOrderAdapter(
        adapterTypeFactory: SomListAdapterTypeFactory
) : BaseListAdapter<Visitable<SomListAdapterTypeFactory>, SomListAdapterTypeFactory>(adapterTypeFactory) {
    fun updateOrders(items: List<Visitable<SomListAdapterTypeFactory>>) {
        val diffCallback = SomListOrderDiffUtilCallback(visitables, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(items)
    }

    fun updateOrder(newOrder: SomListOrderUiModel) {
        visitables.filterIsInstance<SomListOrderUiModel>().indexOfFirst { it.orderId == newOrder.orderId }.let { index ->
            if (index in 0 until visitables.size) {
                val oldOrder = visitables[index]
                visitables[index] = newOrder
                notifyItemChanged(index, oldOrder to newOrder)
            }
        }
    }

    fun removeOrder(orderId: String) {
        visitables.filterIsInstance<SomListOrderUiModel>().indexOfFirst { it.orderId == orderId }.let { index ->
            if (index in 0 until visitables.size) {
                visitables.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }
}