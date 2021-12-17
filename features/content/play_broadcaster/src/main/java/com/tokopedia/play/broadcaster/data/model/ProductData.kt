package com.tokopedia.play.broadcaster.data.model

import com.tokopedia.play.broadcaster.type.ProductPrice
import com.tokopedia.play.broadcaster.type.ProductStock

/**
 * Created by jegul on 24/06/20
 */
data class ProductData(
        val id: String,
        val name: String,
        val imageUrl: String,
        val originalImageUrl: String,
        val stock: ProductStock,
        val price: ProductPrice,
)