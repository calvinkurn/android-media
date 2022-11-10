package com.tokopedia.content.common.producttag.analytic.autocomplete

import android.net.Uri
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
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
        try {
            val appLink = (searchComponentTracking as BaseSuggestionDataView).applink
            val shopId = extractShopId(appLink)
            analytic.clickSuggestionShop(shopId)
        }
        catch (e: Exception) { }
    }

    override fun eventClickKeyword(
        label: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking
    ) {
        super.eventClickKeyword(label, pageSource, searchComponentTracking)
        analytic.clickSuggestionKeyword()
    }

    override fun eventImpressCurated(
        label: String,
        campaignCode: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking
    ) {
        super.eventImpressCurated(label, campaignCode, pageSource, searchComponentTracking)
    }

    override fun eventImpressionSuggestion(searchComponentTracking: SearchComponentTracking) {
        super.eventImpressionSuggestion(searchComponentTracking)
        try {
            val data = (searchComponentTracking as BaseSuggestionDataView)
            if(data.type == "shop") {
                analytic.impressSuggestionShop(extractShopId(data.applink))
            }
        }
        catch (e: Exception) { }
    }

    private fun extractShopId(appLink: String): String {
        return try {
            Uri.parse(appLink).lastPathSegment ?: ""
        }
        catch (e: Exception) { "" }
    }
}