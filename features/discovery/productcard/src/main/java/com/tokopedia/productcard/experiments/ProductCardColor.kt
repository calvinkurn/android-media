package com.tokopedia.productcard.experiments

import com.tokopedia.unifyprinciples.ColorMode

interface ProductCardColor {
    val cardBackgroundColor: Int
    val productNameColor: Int
    val productPriceColor: Int
    val productSlashPriceColor: Int
    val productSoldCountColor: Int
    val productDiscountColor: Int
    val productRatingColor: Int
}
