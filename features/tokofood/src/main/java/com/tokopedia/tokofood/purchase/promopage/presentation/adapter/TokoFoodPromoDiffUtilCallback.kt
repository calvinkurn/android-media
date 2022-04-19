package com.tokopedia.tokofood.purchase.promopage.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.*

class TokoFoodPromoDiffUtilCallback(private val oldList: List<Any>,
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
            oldItem is TokoFoodPromoEligibilityHeaderUiModel && newItem is TokoFoodPromoEligibilityHeaderUiModel -> oldItem == newItem
            oldItem is TokoFoodPromoHeaderUiModel && newItem is TokoFoodPromoHeaderUiModel -> oldItem == newItem
            oldItem is TokoFoodPromoItemUiModel && newItem is TokoFoodPromoItemUiModel -> oldItem == newItem
            oldItem is TokoFoodPromoTickerUiModel && newItem is TokoFoodPromoTickerUiModel -> oldItem == newItem
            else -> false
        }
    }

}
