package com.tokopedia.shareexperience.data.analytic

object ShareExAnalyticsConst {

    object Key {
        const val EVENT = "event"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_LABEL = "eventLabel"
        const val TRACKER_ID = "trackerId"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val PRODUCT_ID = "productId"
        const val USER_ID = "userId"
        const val PROMOTIONS = "promotions"
        const val CREATIVE_NAME = "creative_name"
        const val CREATIVE_SLOT = "creative_slot"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
    }

    object Default {
        const val SHARING_EXPERIENCE = "sharingexperience"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val NOT_SET = "notset"
    }

    object Event {
        const val CLICK_COMMUNICATION = "clickCommunication"
        const val VIEW_COMMUNICATION = "viewCommunicationIris"
        const val VIEW_ITEM = "view_item"
    }

    object Action {
        const val CLICK_SHARE_BUTTON = "click - share button"
        const val CLICK_CLOSE = "click - close share bottom sheet"
        const val CLICK_SHARING_CHANNEL = "click - sharing channel"
        const val VIEW_SHARING_CHANNEL = "view on sharing channel"
        const val IMPRESSION_TICKER_AFFILIATE = "impression - ticker affiliate"
        const val CLICK_TICKER_AFFILIATE = "click - ticker affiliate"
    }

    object Category {
        const val TOP_NAV_PDP = "top nav - product detail page"
        const val PDP = "product detail page"
    }

    object Tracker {
        const val ID_10365 = "10365"
        const val ID_10366 = "10366"
        const val ID_10367 = "10367"
        const val ID_10368 = "10368"
        const val ID_31185 = "31185"
        const val ID_31186 = "31186"
    }
}
