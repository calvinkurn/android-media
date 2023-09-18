package com.tokopedia.stories.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.track.TrackApp

internal const val EVENT = "event"
internal const val EVENT_ACTION = "eventAction"
internal const val EVENT_CATEGORY = "eventCategory"
internal const val EVENT_LABEL = "eventLabel"
internal const val TRACKER_ID = "trackerId"
internal const val BUSINESS_UNIT = "businessUnit"
internal const val CURRENT_SITE = "currentSite"
internal const val SESSION_IRIS = "sessionIris"
internal const val USER_ID = "userId"
internal const val PROMOTIONS = "promotions"
internal const val CREATIVE_NAME = "creative_name"
internal const val CREATIVE_SLOT = "creative_slot"
internal const val ITEM_ID = "item_id"
internal const val ITEM_NAME = "item_name"

internal val isSellerApp = GlobalConfig.isSellerApp()

internal val currentSite: String
    get() = if (isSellerApp) {
        CurrentSite.tokopediaSeller
    } else {
        CurrentSite.tokopediaMarketplace
    }

internal val sessionIris: String
    get() = TrackApp.getInstance().gtm.irisSessionId
