package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getProductById
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getUnavailableReasonUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.*

object VisitableDataHelper {

    private const val INDEX_BEFORE_FROM_UNAVAILABLE_ACCORDION = 1
    private const val INDEX_AFTER_FROM_UNAVAILABLE_SECTION = 2

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

    fun MutableList<Visitable<*>>.getProductByCartId(cartId: String): Pair<Int, TokoFoodPurchaseProductTokoFoodPurchaseUiModel>? {
        loop@ for ((index, data) in this.withIndex()) {
            when {
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && data.cartId == cartId -> {
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
        var firstItemIndex = RecyclerView.NO_POSITION
        val unavailableProducts = mutableListOf<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>()
        loop@ for ((index, data) in this.withIndex()) {
            when {
                data is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !data.isAvailable -> {
                    if (firstItemIndex == RecyclerView.NO_POSITION) firstItemIndex = index
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

    fun MutableList<Visitable<*>>.getProductWithChangedQuantity(): List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel> {
        return this.filterIsInstance<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>()
            .filter { it.isAvailable && it.isEnabled && it.isQuantityChanged }
    }

    fun MutableList<Visitable<*>>.isLastAvailableProduct(): Boolean {
        val count = this.count { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && it.isAvailable }
        return count == Int.ONE
    }

    fun TokoFoodPurchaseProductTokoFoodPurchaseUiModel.getUpdatedCartId(cartTokoFoodData: CartTokoFoodData): String? {
        return cartTokoFoodData.carts.find { cartData ->
            cartData.productId == this.id && cartData.getMetadata()?.variants?.let { variants ->
                var isSameVariants = true
                run checkVariant@ {
                    variants.forEach { variant ->
                        this.variantsParam.any { productVariant ->
                            variant.variantId == productVariant.variantId && variant.optionId == productVariant.optionId
                        }.let { isAnyVariantSame ->
                            if (!isAnyVariantSame) {
                                isSameVariants = false
                                return@checkVariant
                            }
                        }
                    }
                }
                variants.isEmpty() || isSameVariants && variants.size == this.variantsParam.size
            } != false
        }?.cartId
    }

    fun MutableList<Visitable<*>>.setCollapsedUnavailableProducts(dataList: MutableList<Visitable<*>>,
                                                                  mAccordionData: Pair<Int, TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>) {
        val unavailableReasonData = dataList.getUnavailableReasonUiModel()
        unavailableReasonData?.let { mUnavailableReasonData ->
            val from = mUnavailableReasonData.first + INDEX_AFTER_FROM_UNAVAILABLE_SECTION
            val to = mAccordionData.first - INDEX_BEFORE_FROM_UNAVAILABLE_ACCORDION
            clear()
            addAll(dataList.subList(from, to).toMutableList())
        }
    }

    fun MutableList<Visitable<*>>?.setCartId(index: Int, cartId: String) {
        (this?.getOrNull(index) as? TokoFoodPurchaseProductTokoFoodPurchaseUiModel)?.cartId = cartId
    }

}
