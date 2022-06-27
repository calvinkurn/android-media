package com.tokopedia.oldproductbundle.single.presentation.adapter

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.oldproductbundle.single.presentation.model.SingleProductBundleSelectedItem

interface BundleItemListener {
    fun onVariantSpinnerClicked(selectedVariant: ProductVariant?, selectedProductId: String?)
    fun onBundleItemSelected(
        originalPrice: Double,
        discountedPrice: Double,
        quantity: Int,
        preorderDurationWording: String?
    )
    fun onDataChanged(
        selectedData: List<SingleProductBundleSelectedItem>,
        selectedProductVariant: ProductVariant?
    )
}
