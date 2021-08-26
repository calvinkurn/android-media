package com.tokopedia.product_bundle.multiple.presentation.model

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

class ProductBundleDetail(
    val productId: Long = 0L,
    val productImageUrl: String = "",
    val productName: String = "",
    var originalPrice: Double = 0.0,
    var bundlePrice: Double = 0.0,
    var discountAmount: Int = 0,
    var productVariant: ProductVariant? = null,
    var selectedVariantText: String =  "",
    var mapOfSelectedVariantOption: MutableMap<String, String>? = null
) {
    val hasVariant: Boolean = (productVariant != null)
    fun getVariantChildFromProductId(productId: String) = productVariant?.children?.find {
        it.productId == productId
    }
}