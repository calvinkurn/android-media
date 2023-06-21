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
        const val MEDAL_PROGRESS_PERCENTAGE = "progress_pct"
        const val MEDAL_PROGRESS_TASK_COMPLETED = "progress_task_completed"
        const val MEDAL_COUPON_PROMO_CODE = "coupon_promo_code"
        const val MEDAL_COUPON_STATUS = "bonus_status"
        const val MEDAL_COUPON_NOTES = "coupon_notes"
        const val MEDAL_TICKER_STATUS = "ticker_status"
        const val MEDAL_CTA_TEXT = "cta_text"
    }

    object General {
        const val VIEW_PAGE_EVENT = "view page"
        const val SOURCE_OTHER_PAGE = "medali_other_page"
        const val MDP_EVENT_ACTION = "medali detail page"
        const val BACK_BUTTON_CLICK = "click back"
        const val CTA_CLICK = "click cta"
    }

    object Category {
        const val MDP_NON_WHITELISTED = "medali detail page - non whitelisted"
        const val MEDAL_CELEBRATION_NON_WHITELISTED = "goto medali - celebration - non whitelisted"
    }

    object Tracker {
        const val MDP_VIEW_PAGE = "44004"
        const val MDP_CLICK_BACK = "44005"
        const val MDP_CLICK_TNC = "44006"
        const val MDP_VIEW_MEDAL = "44007"
        const val MDP_CLICK_MEDAL = "44008"
        const val MDP_VIEW_MEDAL_PROGRESS = "44012"
        const val MDP_VIEW_MEDAL_BONUS = "44013"
        const val MDP_VIEW_SEE_CABINET_CTA = "44015"
        const val MDP_CLICK_SEE_CABINET_CTA = "44016"
        const val MDP_VIEW_PRIMARY_CTA = "44017"
        const val MDP_CLICK_PRIMARY_CTA = "44018"
        const val MDP_VIEW_COUPON_SUCCESS_TOASTER = "44021"
        const val MDP_VIEW_COUPON_LOAD_ERROR = "44023"
        const val MDP_VIEW_PAGE_SHIMMER = "44024"
        const val MDP_VIEW_PAGE_NON_WHITELISTED = "44787"
        const val MDP_NON_WHITELISTED_CTA_CLICK = "44788"
        const val MDP_NON_WHITELISTED_BACK_CLICK = "44789"
        const val MEDAL_CELEBRATION_VIEW_PAGE_NON_WHITELISTED = "44769"
        const val MEDAL_CELEBRATION_NON_WHITELISTED_CTA_CLICK = "44776"
        const val MEDAL_CELEBRATION_NON_WHITELISTED_BACK_CLICK = "44777"
    }
}
