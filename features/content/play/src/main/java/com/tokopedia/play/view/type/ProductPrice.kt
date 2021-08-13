package com.tokopedia.play.view.type

import com.tokopedia.play.analytic.TrackingField

/**
 * Created by jegul on 03/03/20
 */
sealed class ProductPrice

data class OriginalPrice(
        val price: String,

        @TrackingField
        val priceNumber: Double
) : ProductPrice()

data class DiscountedPrice(
        val originalPrice: String,
        val discountPercent: Int,
        val discountedPrice: String,

        @TrackingField
        val discountedPriceNumber: Double
) : ProductPrice()