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

    object EventCategory {
        const val MEDAL_CABINET = "medali homepage cabinet"
        const val UNLOCKED_MEDAL_PAGE = "unlocked medali page"
        const val LOCKED_MEDAL_PAGE = "locked medali page"
        const val MEDAL_CABINET_ERROR = "medali homepage cabinet - full API error"
        const val MEDAL_CABINET_SKELETON = "medali homepage cabinet - skeleton"
        const val MEDAL_CABINET_INTERNET_ERROR = "medali homepage cabinet - internet error"
        const val MEDAL_CABINET_NON_WHITELISTED = "medali homepage cabinet - non whitelisted user"
        const val UNLOCKED_MEDAL_PAGE_NON_WHITELISTED = "unlocked medali page - non whitelisted user"
        const val LOCKED_MEDAL_PAGE_NON_WHITELISTED = "locked medali page - non whitelisted user"
        const val MDP_NON_WHITELISTED = "medali detail page - non whitelisted"
        const val MEDAL_CELEBRATION_NON_WHITELISTED = "goto medali - celebration - non whitelisted"
    }

    object EventAction {
        const val CLICK_CTA = "click cta"
        const val VIEW_UNLOCKED_MEDAL = "view unlocked medali"
        const val CLICK_UNLOCKED_MEDAL = "click unlocked medali"
        const val VIEW_UNLOCKED_MEDAL_GENERIC = "view unlocked medali - generic"
        const val VIEW_UNLOCKED_MEDAL_SECTION_CTA = "view unlocked medali section CTA"
        const val CLICK_UNLOCKED_MEDAL_SECTION_CTA = "click unlocked medali section CTA"
        const val VIEW_LOCKED_MEDAL = "view locked medali"
        const val CLICK_LOCKED_MEDAL = "click locked medali"
        const val VIEW_LOCKED_MEDAL_SECTION_CTA = "view locked medali section CTA"
        const val CLICK_LOCKED_MEDAL_SECTION_CTA = "click locked medali section CTA"
        const val VIEW_BANNER = "view banner"
        const val CLICK_BANNER = "click banner"
        const val VIEW_UNLOCKED_MEDAL_SECTION_ERROR = "unlocked medali section - API error - view page"
        const val CLICK_CTA_UNLOCKED_MEDAL_SECTION_ERROR = "unlocked medali section - API error - click CTA"
        const val VIEW_LOCKED_MEDAL_SECTION_ERROR = "locked medali section - API error - view page"
        const val CLICK_CTA_LOCKED_MEDAL_SECTION_ERROR = "locked medali section - API error - click CTA"
        const val CLICK_CTA_HALAMAN_UTAMA = "click halaman utama"
        const val CLICK_CTA_COBA_LAGI = "click coba lagi"
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
        const val MEDAL_BONUS_TEXT = "bonus_text"
        const val MEDAL_BUTTON_TEXT = "button_text"
        const val MEDAL_TICKER_STATUS = "ticker_status"
        const val MEDAL_CTA_TEXT = "cta_text"
        const val CREATIVE_NAME = "creative_name"
        const val BANNER_POSITION = "banner_position"
    }

    object General {
        const val VIEW_PAGE_EVENT = "view page"
        const val SOURCE_OTHER_PAGE = "medali_other_page"
        const val MDP_EVENT_ACTION = "medali detail page"
        const val BACK_BUTTON_CLICK = "click back"
        const val CTA_CLICK = "click cta"
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

        const val CABINET_PAGE_VIEW = 44256
        const val CABINET_PAGE_CLICK_BACK = 44257
        const val CABINET_PAGE_CLICK_UNLOCKED_MEDAL = 44258
        const val CABINET_PAGE_VIEW_UNLOCKED_MEDAL = 44259
        const val CABINET_PAGE_VIEW_GENERIC_MEDAL = 44260
        const val CABINET_PAGE_VIEW_CTA_SEE_MORE_UNLOCKED_MEDAL = 44261
        const val CABINET_PAGE_CLICK_CTA_SEE_MORE_UNLOCKED_MEDAL = 44262
        const val CABINET_PAGE_VIEW_LOCKED_MEDAL = 44263
        const val CABINET_PAGE_CLICK_LOCKED_MEDAL = 44264
        const val CABINET_PAGE_VIEW_CTA_SEE_MORE_LOCKED_MEDAL = 44265
        const val CABINET_PAGE_CLICK_CTA_SEE_MORE_LOCKED_MEDAL = 44266
        const val UNLOCKED_MEDAL_PAGE_VIEW = 44267
        const val UNLOCKED_MEDAL_PAGE_VIEW_MEDAL = 44268
        const val UNLOCKED_MEDAL_PAGE_CLICK_MEDAL = 44269
        const val UNLOCKED_MEDAL_PAGE_CLICK_BACK = 44270
        const val LOCKED_MEDAL_PAGE_VIEW = 44271
        const val LOCKED_MEDAL_PAGE_VIEW_MEDAL = 44272
        const val LOCKED_MEDAL_PAGE_CLICK_MEDAL = 44273
        const val LOCKED_MEDAL_PAGE_CLICK_BACK = 44274
        const val CABINET_PAGE_VIEW_EMPTY_BANNER = 44277
        const val CABINET_PAGE_CLICK_EMPTY_BANNER = 44278
        const val CABINET_PAGE_VIEW_UNLOCKED_SECTION_API_ERROR = 44279
        const val CABINET_PAGE_UNLOCKED_SECTION_API_ERROR_CLICK_COBA_LAGI = 44280
        const val CABINET_PAGE_VIEW_LOCKED_SECTION_API_ERROR = 44281
        const val CABINET_PAGE_LOCKED_SECTION_API_ERROR_CLICK_COBA_LAGI = 44282
        const val CABINET_PAGE_VIEW_API_ERROR = 44283
        const val CABINET_PAGE_API_ERROR_CLICK_COBA_LAGI = 44284
        const val CABINET_PAGE_API_ERROR_CLICK_HALAMAN_UTAMA = 44285
        const val CABINET_PAGE_API_ERROR_CLICK_BACK = 44286
        const val CABINET_PAGE_VIEW_SKELETON = 44287
        const val CABINET_PAGE_VIEW_INTERNET_ERROR = 44288
        const val CABINET_PAGE_INTERNET_ERROR_CLICK_COBA_LAGI = 44289
        const val CABINET_PAGE_INTERNET_ERROR_CLICK_BACK = 44290
        const val CABINET_PAGE_VIEW_NON_WHITELISTED_ERROR = 44326
        const val CABINET_PAGE_NON_WHITELISTED_ERROR_CLICK_CTA = 44333
        const val CABINET_PAGE_NON_WHITELISTED_ERROR_CLICK_BACK = 44334
        const val UNLOCKED_MEDAL_PAGE_VIEW_NON_WHITELISTED_ERROR = 44756
        const val UNLOCKED_MEDAL_PAGE_NON_WHITELISTED_ERROR_CLICK_CTA = 44757
        const val UNLOCKED_MEDAL_PAGE_NON_WHITELISTED_ERROR_CLICK_BACK = 44758
        const val LOCKED_MEDAL_PAGE_VIEW_NON_WHITELISTED_ERROR = 44759
        const val LOCKED_MEDAL_PAGE_NON_WHITELISTED_ERROR_CLICK_CTA = 44760
        const val LOCKED_MEDAL_PAGE_NON_WHITELISTED_ERROR_CLICK_BACK = 44761
    }
}
