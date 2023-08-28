package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseAddressTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseDividerTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchasePromoTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseShippingTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel

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
            oldItem is TokoFoodPurchasePromoTokoFoodPurchaseUiModel && newItem is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> {
                oldItem == newItem && oldItem.isLoading == newItem.isLoading
            }
            oldItem is TokoFoodPurchaseShippingTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseShippingTokoFoodPurchaseUiModel -> {
                oldItem == newItem && oldItem.isLoading == newItem.isLoading
            }
            oldItem is TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel -> {
                oldItem == newItem && oldItem.isLoading == newItem.isLoading
            }
            oldItem is TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel -> oldItem == newItem
            oldItem is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel && newItem is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel -> {
                oldItem == newItem && oldItem.isLoading == newItem.isLoading && oldItem.isButtonLoading == newItem.isButtonLoading
            }
            else -> false
        }
    }

}
