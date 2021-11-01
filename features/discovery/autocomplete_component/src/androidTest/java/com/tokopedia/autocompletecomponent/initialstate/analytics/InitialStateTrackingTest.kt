package com.tokopedia.autocompletecomponent.initialstate.analytics

import android.util.Log
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking
import com.tokopedia.iris.Iris

class InitialStateTrackingTest(iris: Iris): InitialStateTracking(iris) {

    override fun eventClickRefreshPopularSearch() {
        super.eventClickRefreshPopularSearch()
        Log.d("TestTracking", "Click refresh popular search")
    }
}