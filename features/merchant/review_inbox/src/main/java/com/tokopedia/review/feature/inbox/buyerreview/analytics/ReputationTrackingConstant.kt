package com.tokopedia.review.feature.inbox.buyerreview.analytics

/**
 * Created by zulfikarrahman on 3/13/18.
 */
object ReputationTrackingConstant {
    //Event
    val CLICK_REVIEW_OLD: String = "clickReview"
    val VIEW_REVIEW: String = "viewReviewIris"

    //Category
    val REVIEW_DETAIL_PAGE: String = "review detail page"
    val REVIEW_PAGE: String = "ulasan page"
    val SELLER_FEEDBACK_PAGE: String = "seller feedback page"
    @kotlin.jvm.JvmField
    val WAITING_REVIEWED: String = "menunggu diulas"
    val INVOICE: String = "invoice"

    //Action
    val CLICK_BACK_BUTTON_SELLER_FEEDBACK: String = "click - back button on seller feedback page"
    val VIEW_SELLER_FEEDBACK: String = "view - seller feedback page"
    val CLICK_SMILEY: String = "click - smiley "
    val CLICK_OVERFLOW_MENU: String = "click - three dots menu on product review"
    val CLICK_OVERFLOW_MENU_SHARE: String = "click - bagikan on three dots menu"
    val CLICK_SEE_REPLY_TEXT: String = "click - lihat balasan on product"
    val VIEW_OVO_INCENTIVES_TICKER: String = "view - ovo incentives ticker"
    val CLICK_READ_SK_OVO_INCENTIVES_TICKER: String = "click - baca s&k on ovo incentives ticker"
    val CLICK_DISMISS_OVO_INCENTIVES_TICKER: String = "click - dismiss ovo incentives ticker"
    val CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET: String =
        "click - dismiss ovo incentives s&k bottomsheet"
    val CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES: String =
        "click - lanjut kirim ulasan on ovo incentives s&k bottomsheet"
    val DELETE: String = "delete"
    val MESSAGE: String = "message:"
    val REVIEW_DETAIL_PAGE_SCREEN: String = "review detail page - report abuse"

    //key
    val PRODUCT_ID: String = "product_id"
    val EVENT: String = "event"
    val EVENT_CATEGORY: String = "eventCategory"
    val EVENT_ACTION: String = "eventAction"
    val EVENT_LABEL: String = "eventLabel"
    val SCREEN_NAME: String = "screenName"
}