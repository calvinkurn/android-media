package com.tokopedia.checkers

object ProductDetailViewsChecker {
    fun onlyViewItem(event: String?) =
            event?.toLowerCase()?.contains("view_item") ?: false

    fun isOnlyOneProduct(items: List<Any>) = items.size == 1
}