package com.tokopedia.product_bundle.single.presentation.model

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

data class SingleProductBundleUiModel (
        var preorderDurationWording: String? = null,
        var items: List<SingleProductBundleItem> = emptyList(),
        var selectedItems: List<SingleProductBundleSelectedItem> = emptyList()
)

data class TotalAmountUiModel (
        var price: String = "Rp0",
        var slashPrice: String = "Rp0",
        var priceGap: String = "Rp0",
        var discount: Int = 0,
)

data class SingleProductBundleItem (
        var quantity: Int = 0,
        var bundleName: String = "",
        var productName: String = "",
        var price: Double = 0F.toDouble(),
        var slashPrice: Double = 0F.toDouble(),
        var discount: Int = 0,
        var imageUrl: String = "",
        var selectedVariantText: String = "",
        var productVariant: ProductVariant? = null
) {
        val hasVariant: Boolean = (productVariant != null)

        fun getVariantChildFromProductId(productId: String) = productVariant?.children?.find {
                it.productId == productId
        }
}

data class SingleProductBundleSelectedItem (
        var productId: String = "",
        var isSelected: Boolean = false
)