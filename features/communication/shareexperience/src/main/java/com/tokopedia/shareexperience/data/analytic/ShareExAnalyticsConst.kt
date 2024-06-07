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
        const val SHOP_ID = "shopId"
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
        const val REVIEW = "fullscreen review"
        const val GOPAYLATER_REFERRAL = "gopaylater referral"
        const val ORDER_DETAIL = "order detail history"
        const val THANK_YOU_PAGE = "share - thank you page"
        const val GROUP_CHAT_ROOM = "groupchat room"
    }

    object Tracker {
        // PDP
        const val ID_10365 = "10365"
        const val ID_10366 = "10366"
        const val ID_10367 = "10367"
        const val ID_10368 = "10368"
        const val ID_31185 = "31185"
        const val ID_31186 = "31186"

        // Review
        const val ID_50124 = "50124"
        const val ID_50125 = "50125"
        const val ID_50126 = "50126"
        const val ID_50127 = "50127"

        // Gopaylater Referral
        const val ID_50464 = "50464"
        const val ID_50465 = "50465"
        const val ID_50466 = "50466"
        const val ID_50467 = "50467"

        // Order Detail
        const val ID_45653 = "45653"
        const val ID_45654 = "45654"
        const val ID_45655 = "45655"
        const val ID_45656 = "45656"
        const val ID_50278 = "50278"
        const val ID_50279 = "50279"

        // TYP
        const val ID_45899 = "45899"
        const val ID_45900 = "45900"
        const val ID_45901 = "45901"
        const val ID_50879 = "50879"

        // PLAY
        const val ID_10455 = "10455"
        const val ID_10456 = "10456"
        const val ID_10457 = "10457"
        const val ID_10458 = "10458"
    }
}
