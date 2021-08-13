package com.tokopedia.product.manage.common.feature.variant.presentation.data

import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.model.Picture
import com.tokopedia.product.manage.common.feature.variant.data.model.Product
import com.tokopedia.product.manage.common.feature.variant.data.model.Selection

data class GetVariantResult(
        val productName: String,
        val variants: List<ProductVariant>,
        val selections: List<Selection>,
        val sizeCharts: List<Picture>
) {

    fun isAllStockEmpty(): Boolean {
        return variants.all { it.isEmpty() }
    }

    fun isEmptyPrimaryVariant(): Boolean {
        return variants.find { it.isPrimary }?.isEmpty() == true
    }

    fun isEmptyOtherVariant(): Boolean {
        return variants.find { !it.isPrimary }?.isEmpty() == true
    }

    fun getMainVariant(): ProductVariant? {
        return variants.find { it.isPrimary && !it.isEmpty() }
    }

    fun getOtherVariant(): ProductVariant? {
        return variants.find { !it.isPrimary && !it.isEmpty() }
    }
}