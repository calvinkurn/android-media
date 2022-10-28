package com.tokopedia.product_bundle.single.presentation.model

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

data class SingleProductBundleUiModel (
        var items: List<SingleProductBundleItem> = emptyList(),
        var selectedItems: List<SingleProductBundleSelectedItem> = emptyList()
) {
        /**
         * Return selected item, or return `null` if there is no selected item
         */
        fun getSelectedSingleProductBundleItem(): SingleProductBundleItem? {
                val selectedIndex = selectedItems.indexOfFirst { it.isSelected }
                return items.getOrNull(selectedIndex)
        }
}

data class TotalAmountUiModel (
        var price: String = "Rp0",
        var slashPrice: String = "Rp0",
        var priceGap: String = "Rp0",
        var discount: Int = 0,
)

data class SingleProductBundleItem (
        var quantity: Int = 0,
        var productName: String = "",
        var originalPrice: Double = 0F.toDouble(),
        var discountedPrice: Double = 0F.toDouble(),
        var discount: Int = 0,
        var imageUrl: String = "",
        var selectedVariantText: String = "",
        var preorderDurationWording: String? = null,
        var productVariant: ProductVariant? = null
) {
        val hasVariant: Boolean = (productVariant != null)

        /**
         * Return VariantChild based on productId, or return `null` if not found
         */
        fun getVariantChildFromProductId(productId: String) = productVariant?.children?.find {
                it.productId == productId
        }
}

data class SingleProductBundleSelectedItem (
        val shopId: String = "",
        var bundleId: String = "",
        var productId: String = "",
        var quantity: Int = 0,
        var isSelected: Boolean = false,
        var isVariantEmpty: Boolean = false,
        var warehouseId: String = "0"
)