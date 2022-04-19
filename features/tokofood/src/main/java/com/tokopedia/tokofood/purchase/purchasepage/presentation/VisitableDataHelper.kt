package com.tokopedia.tokofood.purchase.purchasepage.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*

object VisitableDataHelper {

    fun MutableList<Visitable<*>>.getAddressUiModel(): Pair<Int, TokoFoodPurchaseAddressTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            when (data) {
                is TokoFoodPurchaseAddressTokoFoodPurchaseUiModel -> {
                    return Pair(index, data)
                }
            }
        }
        return null
    }

    fun MutableList<Visitable<*>>.getProductByProductId(productId: String): Pair<Int, TokoFoodPurchaseProductTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            when {
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && data.id == productId -> {
                    return Pair(index, data)
                }
                data is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel || data is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    fun MutableList<Visitable<*>>.getAccordionUiModel(): Pair<Int, TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            when (data) {
                is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel -> {
                    return Pair(index, data)
                }
                is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    fun MutableList<Visitable<*>>.getSummaryTransactionUiModel(): Pair<Int, TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            when (data) {
                is TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel -> {
                    return Pair(index, data)
                }
            }
        }
        return null
    }

    fun MutableList<Visitable<*>>.getTotalAmountUiModel(): Pair<Int, TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            when (data) {
                is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel -> {
                    return Pair(index, data)
                }
            }
        }
        return null
    }

    fun MutableList<Visitable<*>>.getTickerErrorShopLevelUiModel(): Pair<Int, TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            when (data) {
                is TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel -> {
                    return Pair(index, data)
                }
                is TokoFoodPurchaseProductTokoFoodPurchaseUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    fun MutableList<Visitable<*>>.getUnavailableReasonUiModel(): Pair<Int, TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            if (data is TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel) {
                return Pair(index, data)
            }
        }
        return null
    }

    fun MutableList<Visitable<*>>.getAllUnavailableProducts(): Pair<Int, List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>> {
        var firstItemIndex = -1
        val unavailableProducts = mutableListOf<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>()
        loop@ for ((index, data) in this.withIndex()) {
            when {
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && data.isUnavailable -> {
                    if (firstItemIndex == -1) firstItemIndex = index
                    unavailableProducts.add(data)
                }
                data is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel || data is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> {
                    break@loop
                }
            }
        }
        return Pair(firstItemIndex, unavailableProducts)
    }

}