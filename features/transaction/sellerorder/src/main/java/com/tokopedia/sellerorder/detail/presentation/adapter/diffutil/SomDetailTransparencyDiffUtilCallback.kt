package com.tokopedia.sellerorder.detail.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFee

class SomDetailTransparencyDiffUtilCallback(
    private val oldItems: List<BaseTransparencyFee>,
    private val newItems: List<BaseTransparencyFee>,
    private val typeFactory: DetailTransparencyFeeAdapterFactoryImpl
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition)?.type(typeFactory) == newItems.getOrNull(newItemPosition)?.type(typeFactory)
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

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return Pair(oldItem, newItem)
    }

}
