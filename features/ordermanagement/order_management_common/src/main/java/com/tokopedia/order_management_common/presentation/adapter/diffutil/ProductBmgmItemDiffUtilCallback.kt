package com.tokopedia.order_management_common.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel

class ProductBmgmItemDiffUtilCallback(
    private val oldItems: List<ProductBmgmSectionUiModel.ProductUiModel>,
    private val newItems: List<ProductBmgmSectionUiModel.ProductUiModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem?.orderDetailId == newItem?.orderDetailId
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

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return Pair(oldItem, newItem)
    }
}
