package com.tokopedia.productcard.layout.variant

import android.view.View
import com.tokopedia.productcard.ProductCardModel

internal interface VariantLayoutStrategy {
    fun renderVariant(
        willShowVariant: Boolean,
        view: View,
        productCardModel: ProductCardModel,
    )

    val sizeCharLimit: Int

    val extraCharSpace: Int

    val colorLimit: Int

    fun getLabelVariantSizeCount(productCardModel: ProductCardModel, colorVariantTaken: Int): Int

    fun getLabelVariantColorCount(colorVariant: List<ProductCardModel.LabelGroupVariant>): Int
}
