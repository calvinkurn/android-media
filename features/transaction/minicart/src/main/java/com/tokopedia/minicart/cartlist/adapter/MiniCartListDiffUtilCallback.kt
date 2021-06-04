package com.tokopedia.minicart.cartlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.minicart.cartlist.uimodel.*

class MiniCartListDiffUtilCallback(private val oldList: List<Any>,
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
            oldItem is MiniCartAccordionUiModel && newItem is MiniCartAccordionUiModel -> oldItem == newItem
            oldItem is MiniCartProductUiModel && newItem is MiniCartProductUiModel -> oldItem == newItem
            oldItem is MiniCartSeparatorUiModel && newItem is MiniCartSeparatorUiModel -> oldItem == newItem
            oldItem is MiniCartShopUiModel && newItem is MiniCartShopUiModel -> oldItem == newItem
            oldItem is MiniCartTickerErrorUiModel && newItem is MiniCartTickerErrorUiModel -> oldItem == newItem
            oldItem is MiniCartTickerWarningUiModel && newItem is MiniCartTickerWarningUiModel -> oldItem == newItem
            oldItem is MiniCartUnavailableHeaderUiModel && newItem is MiniCartUnavailableHeaderUiModel -> oldItem == newItem
            oldItem is MiniCartUnavailableReasonUiModel && newItem is MiniCartUnavailableReasonUiModel -> oldItem == newItem
            else -> oldItem == newItem
        }
    }

}
