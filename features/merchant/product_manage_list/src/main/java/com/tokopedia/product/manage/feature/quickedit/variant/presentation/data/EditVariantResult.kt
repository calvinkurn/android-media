package com.tokopedia.product.manage.feature.quickedit.variant.presentation.data

import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Picture
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Selection

data class EditVariantResult(
    val productId: String,
    val productName: String,
    val variants: List<ProductVariant>,
    val selections: List<Selection>,
    val sizeCharts: List<Picture>
) {

    fun countVariantStock(): Int {
        var stock = 0
        variants.forEach {
            stock =+ it.stock
        }
        return stock
    }

    fun isVariantActive(): Boolean {
        val activeVariant = variants.find { it.isActive() }
        return activeVariant != null
    }
}