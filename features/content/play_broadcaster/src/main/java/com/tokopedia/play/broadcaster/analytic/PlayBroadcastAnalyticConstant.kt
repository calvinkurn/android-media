package com.tokopedia.play.broadcaster.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.track.TrackApp

/**
 * Created by jegul on 28/04/21
 */
internal const val KEY_TRACK_CATEGORY = "seller broadcast"
internal const val KEY_TRACK_CATEGORY_PLAY = "play broadcast"
internal const val KEY_TRACK_CATEGORY_SELLER = "seller"
internal const val KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER = "shop page - seller"
internal const val KEY_TRACK_CATEGORY_GROUP_CHAT_ROOM = "groupchat room"

internal const val KEY_TRACK_CLICK = "click"
internal const val KEY_TRACK_VIEW = "view"
internal const val KEY_TRACK_SCROLL = "scroll"
internal const val KEY_TRACK_IMPRESSION = "impression"

internal val isSellerApp = GlobalConfig.isSellerApp()

internal val currentSite: String
    get() = if (isSellerApp) {
        CurrentSite.tokopediaSeller
    } else {
        CurrentSite.tokopediaMarketplace
    }

internal val sessionIris: String
    get() = TrackApp.getInstance().gtm.irisSessionId

internal val KEY_TRACK_CLICK_EVENT: String
    get() = if (isSellerApp) {
        Event.clickContent
    } else {
        Event.clickPG
    }

internal val KEY_TRACK_VIEW_EVENT: String
    get() = if (isSellerApp) {
        Event.viewContentIris
    } else {
        Event.viewPGIris
    }

internal fun getTrackerId(trackerIdMA: String, trackerIdSA: String): String {
    return if (isSellerApp) {
        trackerIdSA
    } else {
        trackerIdMA
    }
}
