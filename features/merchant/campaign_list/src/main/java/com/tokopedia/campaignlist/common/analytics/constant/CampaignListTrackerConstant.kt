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
    }

    object Values {
        const val TOKOPEDIA_SELLER = "tokopediaseller"
        const val PHYSICAL_GOODS = "physical goods"
    }

    object Event {
        const val IMPRESSION_CAMPAIGN_LIST = "viewReleasePageIris"
        const val CLICK_RELEASE_PAGE = "clickReleasePage"
    }

    object Action {
        const val IMPRESSION_CAMPAIGN_LIST = "impression active campaign list"
        const val CLICK_CAMPAIGN_FILTER_TYPE = "click filter campaign type"
        const val CLICK_CAMPAIGN_STATUS = "click filter campaign status"
        const val CLICK_CAMPAIGN_STATUS_AVAILABLE = "click filter campaign status - available"
        const val CLICK_CAMPAIGN_STATUS_UPCOMING = "click filter campaign status - upcoming"
        const val CLICK_CAMPAIGN_STATUS_ONGOING = "click filter campaign status - ongoing"
        const val NOT_SPECIFIED = ""
    }

    object EventCategory {
        const val SPECIAL_RELEASE = "special release page"
    }

    object Label {
        const val EMPTY = ""
    }
}

