package com.tokopedia.checkers

import com.tokopedia.abstraction.processor.ProductListClickProduct


object ProductListClickChecker {

    fun notContainWords(eventAction: String?) =
            !(eventAction?.toLowerCase()?.contains("impression") ?: false &&
                    eventAction?.toLowerCase()?.contains("view") ?: false)

    fun onlySelectContent(event: String?) =
            event?.toLowerCase()?.contains("select_content") ?: false

    fun isOnlyOneProduct(items: ArrayList<ProductListClickProduct>) = items.size == 1
}