package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper

object MiniCartMapper {
    const val DEFAULT_PARENT_ID = "0"

    fun getAddToCartQuantity(productId: String, miniCartData: MiniCartSimplifiedData?): Int {
        return miniCartData?.run {
            val miniCartItem = miniCartData.miniCartItems.getMiniCartItemProduct(productId)
            val productParentId = miniCartItem?.productParentId ?: DEFAULT_PARENT_ID

            return if (productParentId != DEFAULT_PARENT_ID) {
                miniCartItems.getMiniCartItemParentProduct(productParentId)?.totalQuantity.orZero()
            } else {
                miniCartItem?.quantity.orZero()
            }
        } ?: HomeLayoutMapper.DEFAULT_QUANTITY
    }
}
