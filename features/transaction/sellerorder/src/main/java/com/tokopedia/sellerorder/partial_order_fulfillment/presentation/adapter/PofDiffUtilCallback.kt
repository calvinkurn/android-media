package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofVisitable

class PofDiffUtilCallback(
    private val oldItems: List<PofVisitable>,
    private val newItems: List<PofVisitable>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].areItemsTheSame(newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].areContentsTheSame(newItems[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return Bundle.EMPTY
    }
}
