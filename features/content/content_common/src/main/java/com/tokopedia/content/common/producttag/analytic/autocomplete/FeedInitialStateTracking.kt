package com.tokopedia.content.common.producttag.analytic.autocomplete

import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.iris.Iris
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
class FeedInitialStateTracking @Inject constructor(
    iris: Iris,
    private val analytic: FeedAutoCompleteAnalytic,
): InitialStateTracking(iris) {

    override fun eventClickRecentSearch(
        searchComponentTracking: SearchComponentTracking,
        label: String,
        pageSource: String
    ) {
        super.eventClickRecentSearch(searchComponentTracking, label, pageSource)
        analytic.clickRecentSearch()
    }

    override fun eventClickRecentShop(
        searchComponentTracking: SearchComponentTracking,
        label: String,
        userId: String,
        pageSource: String
    ) {
        super.eventClickRecentShop(searchComponentTracking, label, userId, pageSource)
        analytic.clickRecentSearch()
    }
}