package com.tokopedia.product.manage.common.feature.variant.presentation.data

import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.model.Picture
import com.tokopedia.product.manage.common.feature.variant.data.model.Selection
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class EditVariantResult(
        val productId: String,
        val productName: String,
        val variants: List<ProductVariant>,
        val selections: List<Selection>,
        val sizeCharts: List<Picture>
) {

    fun countVariantStock(): Int = variants.sumBy { it.stock }

    fun getVariantStatus(): ProductStatus {
        val activeVariant = variants.find { it.isActive() }
        val isVariantActive = activeVariant != null
        return if(isVariantActive) {
            ProductStatus.ACTIVE
        } else {
            ProductStatus.INACTIVE
        }
    }

    fun isAllStockEmpty(): Boolean {
        return variants.all { it.isEmpty() }
    }
}