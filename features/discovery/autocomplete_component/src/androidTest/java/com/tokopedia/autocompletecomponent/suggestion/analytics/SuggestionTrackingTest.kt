package com.tokopedia.autocompletecomponent.suggestion.analytics

import android.util.Log
import com.tokopedia.iris.Iris

class SuggestionTrackingTest(
    iris: Iris
): SuggestionTracking(iris) {

    override fun eventClickKeyword(label: String, pageSource: String) {
        super.eventClickKeyword(label, pageSource)
        Log.d("TestTracking", "Click keyword suggestion")
    }
}