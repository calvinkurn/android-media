package com.tokopedia.play.broadcaster.data.model


/**
 * Created by jegul on 20/07/20
 */
data class SerializableProductData(
        val id: String,
        val name: String,
        val imageUrl: String,
        val originalImageUrl: String,
        val hasStock: Boolean,
        val totalStock: Int,
        val price: String,
        val priceNumber: Double,
        val discountedPrice: String,
        val discountedPriceNumber: Double,
        val discountedPercent: Int
)