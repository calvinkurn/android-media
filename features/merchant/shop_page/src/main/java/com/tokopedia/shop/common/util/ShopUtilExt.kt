package com.tokopedia.shop.common.util

import com.tokopedia.productcard.ProductCardModel

object ShopUtilExt {

    fun ProductCardModel.isButtonAtcShown(): Boolean {
        return this.hasAddToCartButton
    }
}
