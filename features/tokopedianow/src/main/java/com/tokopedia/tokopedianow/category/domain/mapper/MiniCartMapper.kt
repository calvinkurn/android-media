package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.DEFAULT_PRODUCT_PARENT_ID
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper

object MiniCartMapper {
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
