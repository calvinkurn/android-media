package com.tokopedia.buyerorderdetail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.BuyerOrderDetailDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory

@Suppress("UNCHECKED_CAST")
class BuyerOrderDetailAdapter(private val typeFactory: BuyerOrderDetailTypeFactory) : BaseAdapter<BuyerOrderDetailTypeFactory>(typeFactory) {
    fun updateItems(newItems: MutableList<Visitable<BuyerOrderDetailTypeFactory>>) {
        val diffCallback = BuyerOrderDetailDiffUtilCallback(visitables as List<Visitable<BuyerOrderDetailTypeFactory>>, newItems, typeFactory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(newItems)
    }
}