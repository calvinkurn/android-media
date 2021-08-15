package com.tokopedia.product_bundle.single.presentation.adapter

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

interface BundleItemListener {
    fun onVariantSpinnerClicked(selectedVariant: ProductVariant?)
    fun onBundleItemSelected(
        originalPrice: Double,
        discountedPrice: Double,
        quantity: Int,
        preorderDurationWording: String?
    )
}
