package com.tokopedia.scp_rewards.common.constants

object TrackerConstants {
    object Business {
        const val BUSINESS_UNIT = "gotorewards"
        const val CURRENT_SITE = "tokopediamarketplace"
    }

    object Event {
        const val VIEW_EVENT = "viewLGIris"
        const val CLICK_EVENT = "clickLG"
    }

    object EventLabelProperties {
        const val BADGE_ID = "badge_id"
        const val MEDALI_STATUS = "medali_status"
        const val POWERED_BY = "powered_by_text"
        const val MEDAL_TITLE = "medali_title"
        const val MEDAL_DESCRIPTION = "medali_desc"
    }

    object General {
        const val VIEW_PAGE_EVENT = "view page"
        const val SOURCE_OTHER_PAGE = "medali_other_page"
        const val MDP_EVENT_ACTION = "medali detail page"
        const val BACK_BUTTON_CLICK = "click back"
    }
}
