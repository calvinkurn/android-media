package com.tokopedia.discovery.common.analytics

object SearchComponentTrackingConst {

    const val BUSINESSUNIT = "businessUnit"
    const val CAMPAIGNCODE = "campaignCode"
    const val COMPONENT = "component"
    const val CURRENTSITE = "currentSite"
    const val PAGEDESTINATION = "pageDestination"
    const val PAGESOURCE = "pageSource"
    const val SEARCH = "search"
    const val TOKOPEDIAMARKETPLACE = "tokopediamarketplace"
    const val KEYWORD_ID_NAME = "keyword:%s | value_id:%s | value_name:%s"
    const val KEYWORD = "keyword:%s"
    const val NONE = "none"

    object Event {
        const val VIEWSEARCHIRIS = "viewSearchIris"
        const val CLICKSEARCH = "clickSearch"
    }

    object Action {
        const val IMPRESSION = "impression"
        const val CLICK = "click"
        const val CLICK_OTHER_ACTION = "click other action"
    }

    object Category {
        const val SEARCH_COMPONENT = "search component"
    }

    object Component {
        const val INITIAL_STATE_CANCEL_SEARCH = "01.09.00.00"
        const val INITIAL_STATE_MANUAL_ENTER = "01.07.00.00"

        const val AUTO_COMPLETE_CANCEL_SEARCH = "02.12.00.00"
        const val AUTO_COMPLETE_MANUAL_ENTER = "02.01.00.00"
    }

    object Options {
        const val NO_TRACKING = 0
        const val IMPRESSION_ONLY = 1
        const val CLICK_ONLY = 2
        const val IMPRESSION_AND_CLICK = 3
    }
}