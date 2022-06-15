package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.*

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

    fun MutableList<Visitable<*>>.getProductById(productId: String, cartId: String): Pair<Int, TokoFoodPurchaseProductTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            when {
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && data.id == productId && data.cartId == cartId -> {
                    return Pair(index, data)
                }
                data is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel || data is TokoFoodPurchasePromoTokoFoodPurchaseUiModel -> {
                    break@loop
                }
            }
        }
        return null
    }

    fun MutableList<Visitable<*>>.getProductByUpdateParam(param: UpdateProductParam): Pair<Int, TokoFoodPurchaseProductTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            val isMatchingProduct =
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel
                        && data.id == param.productId
                        && data.cartId == param.cartId
                        && data.variantsParam == param.variants
            when {
                isMatchingProduct -> {
                    (data as? TokoFoodPurchaseProductTokoFoodPurchaseUiModel)?.let { productVisitable ->
                        return Pair(index, productVisitable)
                    }
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

    inline fun <reified U: Visitable<*>> MutableList<Visitable<*>>.getUiModelIndex(): Int {
        return indexOfFirst { it is U }
    }

    inline fun <reified U: Visitable<*>> MutableList<Visitable<*>>.getUiModel(): Pair<Int, U>? {
        loop@ for ((index, data) in this.withIndex()) {
            if (data is U) {
                return Pair(index, data)
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
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !data.isAvailable -> {
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

    fun MutableList<Visitable<*>>.getPartiallyLoadedModel(isLoading: Boolean): HashMap<Int, Visitable<*>> {
        val partiallyLoadedModels = hashMapOf<Int, Visitable<*>>()
        forEachIndexed { index, model ->
            if (model is CanLoadPartially) {
                (model.copyWithLoading(isLoading) as? Visitable<*>)?.let { loadingModel ->
                    partiallyLoadedModels[index] = loadingModel
                }
            }
        }
        return partiallyLoadedModels
    }

}