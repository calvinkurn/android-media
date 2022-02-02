package com.tokopedia.search.result.product.inspirationwidget

import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.track.TrackApp

object InspirationWidgetTracking {

    fun trackEventClickInspirationCardOption(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            SearchEventTracking.Event.SEARCH_RESULT,
            SearchEventTracking.Category.SEARCH_RESULT,
            SearchEventTracking.Action.CLICK_INSPIRATION_CARD,
            label
        )
    }
}