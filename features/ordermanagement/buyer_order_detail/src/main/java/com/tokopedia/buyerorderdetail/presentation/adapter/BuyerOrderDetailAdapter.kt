package com.tokopedia.buyerorderdetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.model.BuyProtectionUiModel

class BuyerOrderDetailAdapter(typeFactory: BuyerOrderDetailTypeFactory) : BaseAdapter<BuyerOrderDetailTypeFactory>(typeFactory) {

    fun addItem(item: Visitable<*>) {
        visitables.add(item)
        notifyItemInserted(visitables.size - 1)
    }

    fun addItems(items: List<Visitable<*>>) {
        visitables.addAll(items)
        notifyItemRangeInserted(visitables.size - items.size, visitables.size)
    }

    fun removeBuyProtectionWidget() {
        visitables.indexOfFirst { it is BuyProtectionUiModel }.takeIf { it != -1 }?.let { position ->
            visitables.removeAt(position - 1)
            visitables.removeAt(position - 1)
            visitables.removeAt(position - 1)
            notifyItemRangeRemoved(position - 1, 3)
        }
    }
}