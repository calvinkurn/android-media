package com.tokopedia.content.product.preview.view.adapter.product

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.content.product.preview.data.product.ProductIndicatorUiModel

class ProductIndicatorCallback(
    private val oldList: List<ProductIndicatorUiModel>,
    private val newList: List<ProductIndicatorUiModel>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].selected == newList[newItemPosition].selected
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
