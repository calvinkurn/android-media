package com.tokopedia.tokopedianow.similarproduct.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel

internal object TokoNowSimilarProductBottomSheetMapper {
    private const val PRODUCT_DEFAULT_QTY = 0

    fun MutableList<Visitable<*>>.updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        val miniCartItems = miniCart.miniCartItems
        val productList = filterIsInstance<ProductCardCompactSimilarProductUiModel>()

        miniCartItems.filter { it.value is MiniCartItem.MiniCartItemProduct }
            .forEach { miniCartItem ->
                val miniCartProduct = miniCartItem.value as MiniCartItem.MiniCartItemProduct
                productList.firstOrNull { it.id == miniCartProduct.productId }?.let {
                    val index = productList.indexOf(it)
                    val product = it.copy(quantity = miniCartProduct.quantity)
                    set(index, product)
                }
            }
    }

    fun MutableList<Visitable<*>>.updateDeletedProductQuantity(miniCart: MiniCartSimplifiedData) {
        val miniCartProductIds = miniCart.miniCartItems
            .filter { it.value is MiniCartItem.MiniCartItemProduct }
            .map {
                val cartItem = it.value as MiniCartItem.MiniCartItemProduct
                cartItem.productId
            }

        val productList = filterIsInstance<ProductCardCompactSimilarProductUiModel>()

        productList.forEach {
            if (it.id !in miniCartProductIds) {
                val index = productList.indexOf(it)
                val product = it.copy(quantity = PRODUCT_DEFAULT_QTY)
                set(index, product)
            }
        }
    }
}
