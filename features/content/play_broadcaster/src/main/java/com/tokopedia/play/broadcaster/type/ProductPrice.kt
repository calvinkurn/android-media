package com.tokopedia.play.broadcaster.type

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */

sealed class ProductPrice : Parcelable

@Parcelize
data class OriginalPrice(
    val price: String,
    val priceNumber: Double,
) : ProductPrice()

@Parcelize
data class DiscountedPrice(
    val originalPrice: String,
    val originalPriceNumber: Double,
    val discountPercent: Long,
    val discountedPrice: String,
    val discountedPriceNumber: Double
) : ProductPrice()

@Parcelize
object PriceUnknown : ProductPrice()