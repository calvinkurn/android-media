package com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*

class TokoFoodPurchaseDiffUtilCallback(private val oldList: List<Any>,
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
            oldItem is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseAddressTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseAddressTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseDividerTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseDividerTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseProductTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchasePromoTokoFoodPurchaseUiModel && newItem is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseShippingTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseShippingTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel -> oldItem == newItem
            else -> false
        }
    }

}
