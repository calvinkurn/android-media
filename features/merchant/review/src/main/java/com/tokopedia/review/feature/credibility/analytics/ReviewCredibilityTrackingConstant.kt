package com.tokopedia.review.feature.credibility.analytics

object ReviewCredibilityTrackingConstant {
    const val EVENT_ACTION_CLICK_CTA = "click - cta button"
    const val EVENT_ACTION_CLICK_ACHIEVEMENT_STICKER = "click - leaderboard category sticker"
    const val EVENT_ACTION_IMPRESS_ACHIEVEMENT_STICKER = "impression - leaderboard category sticker"
    const val EVENT_ACTION_CLICK_SEE_MORE_ACHIEVEMENT = "click - cta below sticker"
    const val EVENT_ACTION_CLICK_SEE_PROFILE_BUTTON = "click - lihat profil"

    const val EVENT_CATEGORY_PERSONAL_STATISTICS_BOTTOM_SHEET = "product detail page - review - personal statistics bottomsheet"
    const val EVENT_CATEGORY_OTHERS_STATISTICS_BOTTOM_SHEET = "product detail page - review - others statistics bottomsheet"

    const val EVENT_LABEL_CLICK_PERSONAL_CTA = "value:%s;"
    const val EVENT_LABEL_CLICK_OTHERS_CTA = "value:%s;page_owner:%s;"
    const val EVENT_LABEL_CLICK_PERSONAL_ACHIEVEMENT_STICKER = "category:%s;rank:%d;"
    const val EVENT_LABEL_CLICK_OTHERS_ACHIEVEMENT_STICKER = "category:%s;rank:%d;page_owner:%s;"
    const val EVENT_LABEL_CLICK_PERSONAL_SEE_MORE_ACHIEVEMENT = "text:%s;"
    const val EVENT_LABEL_CLICK_OTHERS_SEE_MORE_ACHIEVEMENT = "text:%s;page_owner:%s;"
    const val EVENT_LABEL_PERSONAL_IMPRESS_ACHIEVEMENT_STICKER = ""
    const val EVENT_LABEL_OTHER_IMPRESS_ACHIEVEMENT_STICKER = "page_owner:%s;"
    const val EVENT_LABEL_CLICK_SEE_PROFILE_BUTTON = "page_owner:%s;"

    const val TRACKER_ID_CLICK_CTA_SELF = "33905"
    const val TRACKER_ID_CLICK_CTA_OTHER = "33908"
    const val TRACKER_ID_CLICK_ACHIEVEMENT_STICKER_SELF = "33903"
    const val TRACKER_ID_CLICK_ACHIEVEMENT_STICKER_OTHER = "33906"
    const val TRACKER_ID_CLICK_SEE_MORE_ACHIEVEMENT_SELF = "33904"
    const val TRACKER_ID_CLICK_SEE_MORE_ACHIEVEMENT_OTHER = "33907"
    const val TRACKER_ID_IMPRESS_ACHIEVEMENT_STICKER_SELF = "33919"
    const val TRACKER_ID_IMPRESS_ACHIEVEMENT_STICKER_OTHER = "33918"
    const val TRACKER_ID_CLICK_SEE_PROFILE_BUTTON = "38269"

    const val ITEM_ID_ACHIEVEMENT_STICKER = "leaderboard category sticker"
}
