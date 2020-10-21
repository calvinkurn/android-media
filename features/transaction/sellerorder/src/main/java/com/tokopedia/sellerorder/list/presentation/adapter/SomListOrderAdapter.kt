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

    fun removeOrderWithId(selectedOrderId: String) {
        val index = visitables.indexOfFirst { it is SomListOrderUiModel && it.orderId == selectedOrderId }
        visitables.removeAt(index)
        notifyItemRemoved(index)
    }
}