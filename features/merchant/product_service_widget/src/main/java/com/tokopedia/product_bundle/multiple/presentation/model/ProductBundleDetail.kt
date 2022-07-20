package com.tokopedia.product_bundle.multiple.presentation.model

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

class ProductBundleDetail(
    val productId: Long = 0L,
    var selectedVariantId: String? = null,
    val productName: String = "",
    val productImageUrl: String = "",
    val productQuantity: Int = 0,
    var originalPrice: Double = 0.0,
    var bundlePrice: Double = 0.0,
    var discountAmount: Int = 0,
    var productVariant: ProductVariant? = null,
    var selectedVariantText: String =  "",
    val warehouseId: String = "0"
) {
    val hasVariant: Boolean = productVariant?.hasVariant ?: false
}