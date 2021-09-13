package com.tokopedia.review.common.analytics

object ReviewInboxTrackingConstants {
    private const val TAB = "tab"
    private const val HISTORY_TAB = "riwayat"
    private const val SELLER_TAB = "ulasan pembeli"
    private const val MY_REVIEW = "ulasan saya"
    const val PENDING_TAB = "menunggu diulas"
    const val EVENT_ACTION_CLICK_REVIEW_PENDING_TAB = "${ReviewTrackingConstant.ACTION_CLICK} - $PENDING_TAB $TAB"
    const val EVENT_ACTION_CLICK_REVIEW_HISTORY_TAB = "${ReviewTrackingConstant.ACTION_CLICK} - $HISTORY_TAB $TAB"
    const val EVENT_ACTION_CLICK_REVIEW_SELLER_TAB = "${ReviewTrackingConstant.ACTION_CLICK} - $SELLER_TAB $TAB"
    const val EVENT_ACTION_CLICK_BACK_BUTTON = "${ReviewTrackingConstant.ACTION_CLICK} - back to ulasan button"
    const val SCREEN_NAME = "/inbox-reputation [$PENDING_TAB]"
    const val MY_REVIEW_SCREEN_NAME = "/inbox-reputation [$MY_REVIEW]"
    const val HISTORY_SCREEN_NAME = "/riwayat"
    const val EVENT_CATEGORY_PENDING_TAB = "${ReviewTrackingConstant.REVIEW_PAGE} - $PENDING_TAB"
    const val EVENT_CATEGORY_HISTORY_TAB = "ulasan page - riwayat"
}