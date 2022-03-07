package com.tokopedia.campaignlist.common.analytics.constant

object CampaignListTrackerConstant {

    object Key {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val CURRENT_SITE = "currentSite"
        const val SHOP_ID = "shopId"
        const val BUSINESS_UNIT = "businessUnit"
        const val USER_ID = "userId"
    }

    object Values {
        const val TOKOPEDIA_SELLER = "tokopediaseller"
        const val PHYSICAL_GOODS = "physical goods"
        const val SHARING_EXPERIENCE = "sharingexperience"
    }

    object Event {
        const val IMPRESSION_CAMPAIGN_LIST = "viewReleasePageIris"
        const val CLICK_RELEASE_PAGE = "clickReleasePage"
        const val CLICK_SHOP_PAGE = "clickShopPage"
        const val CLICK_VIEW_SHOP_PAGE = "viewShopPageIris"
    }

    object Action {
        const val IMPRESSION_CAMPAIGN_LIST = "impression active campaign list"
        const val CLICK_CAMPAIGN_FILTER_TYPE = "click filter campaign type"
        const val CLICK_CAMPAIGN_STATUS = "click filter campaign status"
        const val CLICK_CAMPAIGN_TYPE = "click filter campaign type"
        const val CLICK_CAMPAIGN_STATUS_AVAILABLE = "click filter campaign status - available"
        const val CLICK_CAMPAIGN_STATUS_UPCOMING = "click filter campaign status - upcoming"
        const val CLICK_CAMPAIGN_STATUS_ONGOING = "click filter campaign status - ongoing"
        const val CLICK_SHARE_BUTTON = "click - share button"
        const val CLICK_CLOSE_SHARE_BOTTOM_SHEET = "click - close share bottom sheet"
        const val CLICK_SHARING_CHANNEL = "click - sharing channel"
        const val CLICK_VIEW_SHARING_CHANNEL = "view on sharing channel"
        const val NOT_SPECIFIED = ""
    }

    object EventCategory {
        const val SPECIAL_RELEASE = "special release page"
        const val SHOP_PAGE_NPL = "shop page - rilisan spesial"
    }

    object Label {
        const val EMPTY = ""
    }
}

