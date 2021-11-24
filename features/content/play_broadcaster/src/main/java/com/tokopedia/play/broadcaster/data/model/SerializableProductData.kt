package com.tokopedia.play.broadcaster.data.model

import com.tokopedia.play.broadcaster.type.ProductPrice

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
        val price: ProductPrice
)