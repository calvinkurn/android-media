package com.tokopedia.sellerorder.list.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.list.presentation.adapter.diffutilcallbacks.SomListOrderDiffUtilCallback
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.models.SomListMultiSelectSectionUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel

class SomListOrderAdapter(
        adapterTypeFactory: SomListAdapterTypeFactory
) : BaseListAdapter<Visitable<SomListAdapterTypeFactory>, SomListAdapterTypeFactory>(adapterTypeFactory) {

    private fun findSomListMultiSelectSectionUiModel(): SomListMultiSelectSectionUiModel? {
        return visitables.firstOrNull {
            it is SomListMultiSelectSectionUiModel
        } as? SomListMultiSelectSectionUiModel
    }

    fun updateOrders(items: List<Visitable<SomListAdapterTypeFactory>>) {
        val diffCallback = SomListOrderDiffUtilCallback(visitables.toMutableList(), items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateOrder(newOrder: SomListOrderUiModel) {
        visitables.indexOfFirst { it is SomListOrderUiModel && it.orderId == newOrder.orderId }.let { index ->
            if (index in 0 until visitables.size) {
                val oldOrder = visitables[index]
                visitables[index] = newOrder
                notifyItemChanged(index, oldOrder to newOrder)
            }
        }
    }

    fun removeOrder(orderId: String) {
        visitables.indexOfFirst { it is SomListOrderUiModel && it.orderId == orderId }.let { index ->
            if (index in 0 until visitables.size) {
                visitables.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }

    fun updateSomListMultiSelectSectionUiModel(
        somListMultiSelectSectionUiModel: SomListMultiSelectSectionUiModel
    ) {
        val newItems = ArrayList(visitables)
        if (newItems.any { it is SomListMultiSelectSectionUiModel }) {
            newItems.indexOfFirst {
                it is SomListMultiSelectSectionUiModel
            }.takeIf {
                it != -1
            }?.let { index ->
                newItems[index] = somListMultiSelectSectionUiModel
            }
        } else {
            newItems.add(Int.ZERO, somListMultiSelectSectionUiModel)
        }
        updateOrders(newItems as List<Visitable<SomListAdapterTypeFactory>>)
    }

    fun hasOrder(): Boolean {
        return visitables.any { it is SomListOrderUiModel }
    }

    fun removeMultiSelectSection() {
        findSomListMultiSelectSectionUiModel()?.let { multiSelectSectionUiModel ->
            val newItems = ArrayList(visitables)
            newItems.indexOf(multiSelectSectionUiModel).takeIf { it != -1 }?.let { index ->
                newItems.removeAt(index)
            }
            updateOrders(newItems as List<Visitable<SomListAdapterTypeFactory>>)
        }
    }
}