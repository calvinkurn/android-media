package com.tokopedia.checkers

import com.tokopedia.abstraction.processor.ProductListImpressionProduct
import java.util.regex.Pattern

private val getKlikPattern = Pattern.compile("(?=[K|k]).?(?=[L|l]).?(?=[I|i]).?(?=[K|k]).?")
private val getClickPattern = Pattern.compile("(?=[C|c]).?(?=[L|l]).?(?=[I|i]).?(?=[C|c]).?(?=[K|k]).?")

object ProductListImpressionChecker {
    /**
     * @value shouldn't contain "click" or "klik" or empty string
     */
    fun isContainInvalidTerm(eventAction: String): Boolean {
        val trim_value = eventAction.trim()
        return !(trim_value.isEmpty() ||
                getKlikPattern.matcher(trim_value).find() ||
                getClickPattern.matcher(trim_value).find())
    }

    fun isSizeBiggerThanOne(items: List<ProductListImpressionProduct>) = items.size >= 1

    fun isContainValidTerm(event: String) =
            event.contains("view_item_list") || event.contains("view_search_results")
}