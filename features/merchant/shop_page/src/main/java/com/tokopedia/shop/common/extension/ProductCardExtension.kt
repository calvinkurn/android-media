package com.tokopedia.shop.common.extension

import com.tokopedia.productcard.ProductCardModel

//TODO: Remove this ext function once product card v5 global component has quantity editor capability
fun ProductCardModel.disableDirectPurchaseCapability(): ProductCardModel {
    return this.copy(hasAddToCartButton = false, nonVariant = null, variant = null)
}
