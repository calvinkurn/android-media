package com.tokopedia.checkers

object ProductListClickChecker {

    fun notContainWords(eventAction: String?) =
            !(eventAction?.toLowerCase()?.contains("impression") ?: false ||
                    eventAction?.toLowerCase()?.contains("view") ?: false)

    fun onlySelectContent(event: String?) =
            event?.toLowerCase()?.contains("select_content") ?: false

    fun isOnlyOneProduct(items: List<Any>) = items.size == 1
}
