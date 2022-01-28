package com.tokopedia.play.broadcaster.analytic

import com.tokopedia.config.GlobalConfig

/**
 * Created by jegul on 28/04/21
 */
internal const val KEY_EVENT = "event"

internal const val KEY_EVENT_CATEGORY = "eventCategory"
internal const val KEY_EVENT_ACTION = "eventAction"
internal const val KEY_EVENT_LABEL = "eventLabel"

internal const val KEY_USER_ID = "userId"
internal const val KEY_SHOP_ID = "shopId"

internal const val KEY_BUSINESS_UNIT = "businessUnit"
internal const val KEY_CURRENT_SITE = "currentSite"

private const val KEY_TRACK_CURRENT_SITE = "tokopediaseller"
private const val KEY_TRACK_CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
internal const val KEY_TRACK_BUSINESS_UNIT = "play"
internal const val KEY_TRACK_CATEGORY = "seller broadcast"

internal const val KEY_TRACK_CLICK_EVENT = "clickSellerBroadcast"
internal const val KEY_TRACK_VIEW_EVENT = "viewSellerBroadcastIris"

internal const val KEY_TRACK_CLICK = "click"
internal const val KEY_TRACK_VIEW = "view"
internal const val KEY_TRACK_SCROLL = "scroll"
internal const val KEY_TRACK_IMPRESSION = "impression"

val currentSite: String
    get() = if (GlobalConfig.isSellerApp()) {
        KEY_TRACK_CURRENT_SITE
    } else {
        KEY_TRACK_CURRENT_SITE_MARKETPLACE
    }
