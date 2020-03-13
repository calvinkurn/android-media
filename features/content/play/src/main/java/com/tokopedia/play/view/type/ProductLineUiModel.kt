package com.tokopedia.play.view.type

/**
 * Created by jegul on 03/03/20
 */
sealed class PlayProductUiModel

data class ProductLineUiModel(
        val id: String,
        val imageUrl: String,
        val title: String,
        val stock: ProductStock,
        val isVariantAvailable: Boolean,
        val price: ProductPrice
) : PlayProductUiModel()

object ProductPlaceholderUiModel : PlayProductUiModel()