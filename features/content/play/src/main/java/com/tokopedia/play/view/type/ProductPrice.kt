package com.tokopedia.play.view.type

/**
 * Created by jegul on 03/03/20
 */
sealed class ProductPrice

data class OriginalPrice(
        val price: String,
        val priceNumber: Long
) : ProductPrice()

data class DiscountedPrice(
        val originalPrice: String,
        val discountPercent: Int,
        val discountedPrice: String,
        val discountedPriceNumber: Long
) : ProductPrice()