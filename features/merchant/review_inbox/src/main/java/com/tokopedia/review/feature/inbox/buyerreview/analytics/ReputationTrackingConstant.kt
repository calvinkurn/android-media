package com.tokopedia.review.feature.inbox.buyerreview.analytics

/**
 * Created by zulfikarrahman on 3/13/18.
 */
object ReputationTrackingConstant {

    //Event
    const val CLICK_REVIEW_OLD: String = "clickReview"
    const val VIEW_REVIEW: String = "viewReviewIris"

    //Category
    const val REVIEW_DETAIL_PAGE: String = "review detail page"
    const val REVIEW_PAGE: String = "ulasan page"
    const val SELLER_FEEDBACK_PAGE: String = "seller feedback page"
    const val WAITING_REVIEWED: String = "menunggu diulas"
    const val INVOICE: String = "invoice"

    //Action
    const val CLICK_BACK_BUTTON_SELLER_FEEDBACK: String = "click - back button on seller feedback page"
    const val VIEW_SELLER_FEEDBACK: String = "view - seller feedback page"
    const val CLICK_SMILEY: String = "click - smiley "
    const val CLICK_OVERFLOW_MENU: String = "click - three dots menu on product review"
    const val CLICK_OVERFLOW_MENU_SHARE: String = "click - bagikan on three dots menu"
    const val CLICK_SEE_REPLY_TEXT: String = "click - lihat balasan on product"
    const val VIEW_OVO_INCENTIVES_TICKER: String = "view - ovo incentives ticker"
    const val CLICK_READ_SK_OVO_INCENTIVES_TICKER: String = "click - baca s&k on ovo incentives ticker"
    const val CLICK_DISMISS_OVO_INCENTIVES_TICKER: String = "click - dismiss ovo incentives ticker"
    const val DELETE: String = "delete"
    const val MESSAGE: String = "message:"
    const val REVIEW_DETAIL_PAGE_SCREEN: String = "review detail page - report abuse"

    //key
    const val PRODUCT_ID: String = "product_id"
    const val EVENT: String = "event"
    const val EVENT_CATEGORY: String = "eventCategory"
    const val EVENT_ACTION: String = "eventAction"
    const val EVENT_LABEL: String = "eventLabel"
    const val SCREEN_NAME: String = "screenName"
}