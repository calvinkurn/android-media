package com.tokopedia.play.broadcaster.data.model

/**
 * Created by jegul on 24/06/20
 */
data class ProductData(
        val id: Long,
        val name: String,
        val imageUrl: String,
        val originalImageUrl: String,
        val stock: Int
)