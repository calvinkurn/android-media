package com.tokopedia.play.view.type

/**
 * Created by jegul on 03/03/20
 */
data class ProductLineUiModel(
        val id: String,
        val imageUrl: String,
        val title: String,
        val stock: String = "",
        val isVariantAvailable: Boolean,
        val price: ProductPrice
)