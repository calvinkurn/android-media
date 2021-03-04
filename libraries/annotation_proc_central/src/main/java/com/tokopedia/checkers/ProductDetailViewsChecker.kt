package com.tokopedia.checkers

import com.tokopedia.abstraction.processor.ProductDetailProduct
import com.tokopedia.product.util.processor.Product


object ProductDetailViewsChecker {
    fun onlyViewItem(event: String?) =
            event?.toLowerCase()?.contains("view_item") ?: false

    fun isOnlyOneProduct(items: List<ProductDetailProduct>) = items.size == 1

    fun isOnlyOneProduct_(items: List<Product>) = items.size == 1

    fun checkMap(map: Map<String, String>) = map.isNotEmpty()
}