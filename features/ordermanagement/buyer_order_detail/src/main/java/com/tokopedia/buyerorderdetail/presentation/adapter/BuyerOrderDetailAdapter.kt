package com.tokopedia.buyerorderdetail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory

class BuyerOrderDetailAdapter(typeFactory: BuyerOrderDetailTypeFactory) : BaseAdapter<BuyerOrderDetailTypeFactory>(typeFactory) {
    fun addItem(item: Visitable<*>) {
        visitables.add(item)
        notifyItemInserted(visitables.size - 1)
    }

    fun addItems(items: List<Visitable<*>>) {
        visitables.addAll(items)
        notifyItemRangeInserted(visitables.size - items.size, visitables.size)
    }
}