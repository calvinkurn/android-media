package com.tokopedia.tokofood.purchase.purchasepage.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.*

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
            oldItem is TokoFoodPurchaseAccordionUiModel && newItem is TokoFoodPurchaseAccordionUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseAddressUiModel && newItem is TokoFoodPurchaseAddressUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseDividerUiModel && newItem is TokoFoodPurchaseDividerUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseGeneralTickerUiModel && newItem is TokoFoodPurchaseGeneralTickerUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseProductListHeaderUiModel && newItem is TokoFoodPurchaseProductListHeaderUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseProductUiModel && newItem is TokoFoodPurchaseProductUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseProductUnavailableReasonUiModel && newItem is TokoFoodPurchaseProductUnavailableReasonUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchasePromoUiModel && newItem is TokoFoodPurchasePromoUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseShippingUiModel && newItem is TokoFoodPurchaseShippingUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseSummaryTransactionUiModel && newItem is TokoFoodPurchaseSummaryTransactionUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseTickerErrorShopLevelUiModel && newItem is TokoFoodPurchaseTickerErrorShopLevelUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseTotalAmountUiModel && newItem is TokoFoodPurchaseTotalAmountUiModel -> oldItem == newItem
            else -> false
        }
    }

}
