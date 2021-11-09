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

    object Event {
        const val CLICKSEARCH = "clickSearch"
    }

    object Action {
        const val CLICK_OTHER_ACTION = "click other action"
    }

    object Category {
        const val SEARCH_COMPONENT = "search component"
    }

    object Component {
        const val INITIAL_STATE_CANCEL_SEARCH = "01.09.00.00"
        const val AUTO_COMPLETE_CANCEL_SEARCH = "02.12.00.00"
    }
}