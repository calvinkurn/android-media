package com.tokopedia.content.common.analytic.utils

import com.tokopedia.config.GlobalConfig
import com.tokopedia.track.TrackApp

internal const val KEY_EVENT = "event"

internal const val KEY_EVENT_CATEGORY = "eventCategory"
internal const val KEY_EVENT_ACTION = "eventAction"
internal const val KEY_EVENT_LABEL = "eventLabel"

internal const val KEY_USER_ID = "userId"
internal const val KEY_SHOP_ID = "shopId"

internal const val KEY_BUSINESS_UNIT = "businessUnit"
internal const val KEY_CURRENT_SITE = "currentSite"
internal const val KEY_SESSION_IRIS = "sessionIris"

private const val KEY_TRACK_CURRENT_SITE = "tokopediaseller"
private const val KEY_TRACK_CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
internal const val KEY_TRACK_BUSINESS_UNIT = "play"
internal const val KEY_TRACK_CATEGORY = "seller broadcast"
internal const val KEY_TRACK_CATEGORY_PLAY = "play broadcast"
internal const val KEY_TRACK_CATEGORY_SHOP_PAGE_SELLER = "shop page - seller"
internal const val KEY_TRACK_CATEGORY_GROUP_CHAT_ROOM = "groupchat room"
internal const val KEY_TRACKER_ID = "trackerId"

internal const val KEY_ECOMMERCE = "ecommerce"
internal const val KEY_PROMO_VIEW = "promoView"
internal const val KEY_PROMOTIONS = "promotions"
internal const val KEY_CREATIVE_NAME = "creative_name"
internal const val KEY_CREATIVE_SLOT = "creative_slot"
internal const val KEY_ITEM_ID = "item_id"
internal const val KEY_ITEM_NAME = "item_name"

internal const val KEY_TRACK_CLICK_EVENT_SELLER = "clickContent"
internal const val KEY_TRACK_CLICK_EVENT_MARKETPLACE = "clickPG"
internal const val KEY_TRACK_VIEW_EVENT_SELLER = "viewContentIris"

internal const val KEY_TYPE_CONTENT_SELLER = "content-shop"
internal const val KEY_TYPE_CONTENT_USER = "content-user"
internal const val KEY_TYPE_SELLER = "seller"
internal const val KEY_TYPE_USER = "user"
internal const val KEY_TRACK_CLICK = "click"

internal val isSellerApp = GlobalConfig.isSellerApp()

internal val currentSite: String
    get() = if (isSellerApp) {
        KEY_TRACK_CURRENT_SITE
    } else {
        KEY_TRACK_CURRENT_SITE_MARKETPLACE
    }

internal val sessionIris: String
    get() = TrackApp.getInstance().gtm.irisSessionId

internal val KEY_TRACK_CLICK_EVENT: String
    get() = if (isSellerApp) {
        KEY_TRACK_CLICK_EVENT_SELLER
    } else {
        KEY_TRACK_CLICK_EVENT_MARKETPLACE
    }

internal fun getTrackerId(trackerIdMA: String, trackerIdSA: String): String {
    return if (isSellerApp) trackerIdSA
    else trackerIdMA
}

internal fun getAccountType(accountType: String): String {
    return when (accountType) {
        KEY_TYPE_CONTENT_SELLER  -> KEY_TYPE_SELLER
        KEY_TYPE_CONTENT_USER -> KEY_TYPE_USER
        else -> ""
    }
}

internal fun convertToPromotion(
    creativeName: String,
    creativeSlot: Int,
    itemId: String,
    itemName: String
): HashMap<String, Any> {
    return hashMapOf(
        KEY_CREATIVE_NAME to creativeName,
        KEY_CREATIVE_SLOT to creativeSlot,
        KEY_ITEM_ID to itemId,
        KEY_ITEM_NAME to itemName
    )
}
