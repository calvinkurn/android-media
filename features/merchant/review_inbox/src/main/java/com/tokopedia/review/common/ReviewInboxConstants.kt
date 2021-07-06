package com.tokopedia.review.common

object ReviewInboxConstants {
    const val REVIEW_INBOX_INITIAL_PAGE = 1
    const val REVIEW_INBOX_DATA_PER_PAGE = 10
    const val REVIEW_INBOX_NO_PRODUCTS_BOUGHT_IMAGE = "https://images.tokopedia.net/img/android/review/review_pending_inbox_empty.png"
    const val REVIEW_INBOX_NO_PRODUCTS_SEARCH_IMAGE = "https://ecs7.tokopedia.net/android/others/review_inbox_search_empty.png"
    const val CREATE_REVIEW_ERROR_MESSAGE = "create_review_error"
    const val TAB_WAITING_REVIEW = 1
    const val TAB_MY_REVIEW = 2
    const val TAB_BUYER_REVIEW = 3
    const val EXTRA_FROM_PUSH = "from_notif"
    const val GCM_NOTIFICATION = "GCM_NOTIFICATION"
    const val PARAM_TAB = "tab"
    const val PENDING_TAB = "waiting-review"
    const val HISTORY_TAB = "history"
    const val SELLER_TAB = "seller"
    const val PARAM_SOURCE = "pageSource"
    const val DEFAULT_SOURCE = "app link"

    const val REVIEW_PENDING_TRACE = "review_pending_trace"
    const val REVIEW_PENDING_PLT_PREPARE_METRICS = "review_pending_plt_prepare_metrics"
    const val REVIEW_PENDING_PLT_NETWORK_METRICS = "review_pending_plt_network_metrics"
    const val REVIEW_PENDING_PLT_RENDER_METRICS = "review_pending_plt_render_metrics"
}