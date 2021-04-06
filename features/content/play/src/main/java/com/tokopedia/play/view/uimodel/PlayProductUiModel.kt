package com.tokopedia.play.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.analytic.TrackingField
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
            val applink: String?,
            @TrackingField val impressHolder: ImpressHolder = ImpressHolder()
    ) : PlayProductUiModel()

    object Placeholder : PlayProductUiModel()

    object SeeMore: PlayProductUiModel()
}