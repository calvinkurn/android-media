package com.tokopedia.product_bundle.multiple.presentation.model

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild

class ProductBundleDetail(
    val productId: Long = 0L,
    var selectedVariantId: String? = null,
    val productName: String = "",
    var productImageUrl: String = "",
    val productQuantity: Int = 0,
    var originalPrice: Double = 0.0,
    var bundlePrice: Double = 0.0,
    var discountAmount: Int = 0,
    var productVariant: ProductVariant? = null,
    var selectedVariantText: String =  "",
    val warehouseId: String = "0"
) {
    val hasVariant: Boolean = productVariant?.hasVariant ?: false

    fun VariantChild?.getVariantPicture(): String {
        return this?.picture?.url100.orEmpty()
    }
}
