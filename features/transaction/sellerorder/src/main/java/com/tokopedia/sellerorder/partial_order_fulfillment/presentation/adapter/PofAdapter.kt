package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofVisitable

class PofAdapter(
    typeFactory: PofAdapterTypeFactory
) : BaseAdapter<PofAdapterTypeFactory>(typeFactory) {
    @Suppress("UNCHECKED_CAST")
    fun update(items: List<PofVisitable>) {
        val diffUtilCallback = PofDiffUtilCallback(
            visitables.toMutableList() as List<PofVisitable>,
            items
        )
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}
