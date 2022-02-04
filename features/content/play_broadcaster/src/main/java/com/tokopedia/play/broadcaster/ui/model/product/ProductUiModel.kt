package com.tokopedia.play.broadcaster.ui.model.product

import com.tokopedia.play.broadcaster.type.ProductPrice

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
data class ProductUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val stock: Int,
    val price: ProductPrice,
)