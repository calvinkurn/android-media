package com.tokopedia.minicart.common.widget.shoppingsummary.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryHeaderUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryProductUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummarySeparatorUiModel

class ShoppingSummaryDiffUtilCallback(private val oldList: List<Any>,
                                      private val newList: List<Any>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return when {
            oldItem is ShoppingSummaryHeaderUiModel && newItem is ShoppingSummaryHeaderUiModel -> oldItem == newItem
            oldItem is ShoppingSummaryProductUiModel && newItem is ShoppingSummaryProductUiModel -> oldItem == newItem
            oldItem is ShoppingSummarySeparatorUiModel && newItem is ShoppingSummarySeparatorUiModel -> oldItem == newItem
            else -> false
        }
    }

}