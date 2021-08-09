package com.tokopedia.product_bundle.single.presentation.model

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

data class SingleProductBundleUiModel (
        var preorderDurationWording: String? = null,
        var items: List<SingleProductBundleItem> = emptyList(),
        var price: String = "Rp0",
        var slashPrice: String = "Rp0",
        var priceGap: String = "Rp0",
        var discount: Int = 0,
)

data class SingleProductBundleItem (
        var bundleName: String = "",
        var productName: String = "",
        var price: String = "Rp0",
        var slashPrice: String = "Rp0",
        var discount: Int = 0,
        var imageUrl: String = "",
        var productVariant: ProductVariant? = null
)
