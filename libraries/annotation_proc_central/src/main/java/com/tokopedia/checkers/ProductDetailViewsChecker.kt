package com.tokopedia.checkers

import com.tokopedia.abstraction.processor.ProductListClickProduct


object ProductDetailViewsChecker {
    fun onlyViewItem(event: String?) =
            event?.toLowerCase()?.contains("view_item") ?: false

    fun isOnlyOneProduct(items: ArrayList<ProductListClickProduct>) = items.size == 1
}