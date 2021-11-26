package com.tokopedia.play.broadcaster.type

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
sealed class ProductPrice

data class OriginalPrice(
    val price: String,
    val priceNumber: Double,
): ProductPrice()

data class DiscountedPrice(
    val originalPrice: String,
    val originalPriceNumber: Double,
    val discountPercent: Int,
    val discountedPrice: String,
    val discountedPriceNumber: Double
) : ProductPrice()

object PriceUnknown: ProductPrice()