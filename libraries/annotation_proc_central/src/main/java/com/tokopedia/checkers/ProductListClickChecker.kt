package com.tokopedia.checkers

import java.util.Locale

object ProductListClickChecker {

    fun notContainWords(eventAction: String?) =
            !(eventAction?.lowercase(Locale.getDefault())?.contains("impression") ?: false ||
                    eventAction?.lowercase(Locale.getDefault())?.contains("view") ?: false)

    fun onlySelectContent(event: String?) =
        event?.lowercase(Locale.getDefault())?.contains("select_content") ?: false

    fun isOnlyOneProduct(items: List<Any>) = items.size == 1
}
