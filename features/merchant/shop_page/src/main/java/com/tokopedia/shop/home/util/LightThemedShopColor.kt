package com.tokopedia.shop.home.util

import com.tokopedia.productcard.experiments.ProductCardCustomColor

data class LightThemedShopColor(
    override val cardBackgroundColor: Int,
    override val productNameColor: Int,
    override val productPriceColor: Int,
    override val productSlashPriceColor: Int,
    override val productSoldCountColor: Int,
    override val productDiscountColor: Int,
    override val productRatingColor: Int
) : ProductCardCustomColor
