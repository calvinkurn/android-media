package com.tokopedia.stories.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.track.TrackApp

internal const val STORIES_ROOM_CATEGORIES = "stories room"

internal val isSellerApp = GlobalConfig.isSellerApp()

internal val currentSite: String
    get() = if (isSellerApp) {
        CurrentSite.tokopediaSeller
    } else {
        CurrentSite.tokopediaMarketplace
    }

internal val sessionIris: String
    get() = TrackApp.getInstance().gtm.irisSessionId
