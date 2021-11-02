package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.ProductPrice
import com.tokopedia.play.view.type.ProductStock

/**
 * Created by jegul on 03/03/20
 */
sealed class PlayProductUiModel {

    data class Product(
            val id: String,
            val shopId: String,
            val imageUrl: String,
            val title: String,
            val stock: ProductStock,
            val isVariantAvailable: Boolean,
            val price: ProductPrice,
            val minQty: Int,
            val isFreeShipping: Boolean,
            val applink: String?
    ) : PlayProductUiModel()

    object Placeholder : PlayProductUiModel()

    data class SeeMore(val total: Int): PlayProductUiModel()
}