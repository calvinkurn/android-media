package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper

object AddToCartMapper {

    private const val DEFAULT_PRODUCT_PARENT_ID = "0"
    private const val DEFAULT_PRODUCT_QUANTITY = 0

    fun removeProductAtcQuantity(
        productId: String,
        parentProductId: String,
        miniCartData: MiniCartSimplifiedData,
        updateProductQuantity: (String, Int) -> Unit
    ) {
        if (parentProductId != DEFAULT_PRODUCT_PARENT_ID) {
            val totalQuantity = miniCartData.miniCartItems
                .getMiniCartItemParentProduct(parentProductId)?.totalQuantity.orZero()
            if (totalQuantity == DEFAULT_PRODUCT_QUANTITY) {
                updateProductQuantity(productId, DEFAULT_PRODUCT_QUANTITY)
            } else {
                updateProductQuantity(productId, totalQuantity)
            }
        } else {
            updateProductQuantity(productId, DEFAULT_PRODUCT_QUANTITY)
        }
    }

    fun MiniCartSimplifiedData?.getAddToCartQuantity(productId: String): Int {
        return this?.run {
            val miniCartItem = miniCartItems.getMiniCartItemProduct(productId)
            val productParentId = miniCartItem?.productParentId ?: DEFAULT_PRODUCT_PARENT_ID

            return if (productParentId != DEFAULT_PRODUCT_PARENT_ID) {
                miniCartItems.getMiniCartItemParentProduct(productParentId)?.totalQuantity.orZero()
            } else {
                miniCartItem?.quantity.orZero()
            }
        } ?: HomeLayoutMapper.DEFAULT_QUANTITY
    }
}
