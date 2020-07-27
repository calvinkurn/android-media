package com.tokopedia.product.manage.feature.quickedit.variant.presentation.data

import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Picture
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Selection

data class GetVariantResult(
    val productName: String,
    val variants: List<ProductVariant>,
    val selections: List<Selection>,
    val sizeCharts: List<Picture>
) {

    fun isAllStockEmpty(): Boolean {
        return variants.all { it.isEmpty() }
    }
}