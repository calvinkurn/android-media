package com.tokopedia.autocompletecomponent.suggestion.analytics

import android.util.Log
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.iris.Iris

class SuggestionTrackingTest(
    iris: Iris
): SuggestionTracking(iris) {

    override fun eventClickKeyword(
        label: String,
        pageSource: String,
        searchComponentTracking: SearchComponentTracking
    ) {
        super.eventClickKeyword(label, pageSource, searchComponentTracking)
        Log.d("TestTracking", "Click keyword suggestion")
    }
}