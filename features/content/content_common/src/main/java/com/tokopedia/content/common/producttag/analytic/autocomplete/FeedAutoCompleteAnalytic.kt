package com.tokopedia.content.common.producttag.analytic.autocomplete

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
interface FeedAutoCompleteAnalytic {

    fun clickRecentSearch()

    fun clickSuggestionShop(shopId: String)

    fun impressSuggestionShop(shopId: String)

    fun clickSuggestionKeyword()
}