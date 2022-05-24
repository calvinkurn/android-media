package com.tokopedia.createpost.producttag.analytic.autocomplete

import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.iris.Iris

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
class FeedSuggestionTracking(
    iris: Iris,
    private val analytic: FeedAutoCompleteAnalytic,
) : SuggestionTracking(iris) {

    override fun eventClickShop(
        label: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking
    ) {
        super.eventClickShop(label, pageSource, searchComponentTracking)
        analytic.clickSuggestionShop(label)
    }

    override fun eventClickKeyword(
        label: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking
    ) {
        super.eventClickKeyword(label, pageSource, searchComponentTracking)
        analytic.clickSuggestionKeyword()
    }
}