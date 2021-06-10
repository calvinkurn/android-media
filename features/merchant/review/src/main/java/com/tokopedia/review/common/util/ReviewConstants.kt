package com.tokopedia.review.common.util

/**
 * @author by milhamj on 2020-02-14.
 */

object ReviewConstants {
    const val RESULT_INTENT_DETAIL = 772
    const val RESULT_INTENT_REVIEW_REPLY = 896

    const val LAST_WEEK_KEY = "time=7d"
    const val LAST_MONTH_KEY = "time=30d"
    const val LAST_YEAR_KEY = "time=1y"
    const val ALL_KEY = "time=all"

    const val LAST_WEEK_VALUE = "7 Hari Terakhir"
    const val LAST_MONTH_VALUE = "30 Hari Terakhir"
    const val LAST_YEAR_VALUE = "1 Tahun Terakhir"
    const val ALL_VALUE = "Semua"

    const val MOST_REVIEW_KEY = "review_count desc"
    const val HIGHEST_RATING_KEY = "rating_avg desc"
    const val LOWEST_RATING_KEY = "rating_avg asc"

    const val HIGHEST_RATING_DETAIL_KEY = "rating desc"
    const val LOWEST_RATING_DETAIL_KEY = "rating asc"

    const val LATEST_REVIEW_KEY = "create_time desc"

    const val LATEST_REVIEW_VALUE = "Ulasan Terbaru"

    const val MOST_REVIEW_VALUE = "Ulasan Terbanyak"
    const val HIGHEST_RATING_VALUE = "Rating Tertinggi"
    const val LOWEST_RATING_VALUE = "Rating Terendah"

    const val ANSWERED_KEY = "Sudah dibalas"
    const val ANSWERED_VALUE = "answered"

    const val UNANSWERED_KEY = "Belum dibalas"
    const val UNANSWERED_VALUE = "unanswered"

    const val ALL_RATINGS = "Semua Rating"

    const val prefixStatus = "status="
    const val prefixRating = "rating="

    fun mapSortReviewProduct(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[MOST_REVIEW_KEY] = MOST_REVIEW_VALUE
        map[HIGHEST_RATING_KEY] = HIGHEST_RATING_VALUE
        map[LOWEST_RATING_KEY] = LOWEST_RATING_VALUE
        return map
    }

    fun mapSortReviewDetail(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[LATEST_REVIEW_KEY] = LATEST_REVIEW_VALUE
        map[HIGHEST_RATING_DETAIL_KEY] = HIGHEST_RATING_VALUE
        map[LOWEST_RATING_DETAIL_KEY] = LOWEST_RATING_VALUE
        return map
    }

    fun mapFilterReviewProduct(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[LAST_WEEK_KEY] = LAST_WEEK_VALUE
        map[LAST_MONTH_KEY] = LAST_MONTH_VALUE
        map[LAST_YEAR_KEY] = LAST_YEAR_VALUE
        return map
    }

    fun mapFilterReviewDetail(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[LAST_WEEK_KEY] = LAST_WEEK_VALUE
        map[LAST_MONTH_KEY] = LAST_MONTH_VALUE
        map[LAST_YEAR_KEY] = LAST_YEAR_VALUE
        map[ALL_KEY] = ALL_VALUE
        return map
    }

    const val DEFAULT_PER_PAGE = 15
    const val HAS_TAB_RATING_PRODUCT = "hasTabRatingProduct"
    const val HAS_FILTER_AND_SORT = "hasFilterAndSort"
    const val HAS_OVERALL_RATING_PRODUCT = "hasOverallRatingProduct"
    const val HAS_TICKER_INBOX_REVIEW = "hasTickerInboxReview"
    const val HAS_TICKER_REVIEW_REMINDER = "hasTickerReviewReminder"
    const val HAS_COACHMARK_REMINDER_MESSAGE = "hasCoachmarkReminderMessage"

    const val ARGS_REPUTATION_ID = "ARGS_REPUTATION_ID"
    const val ARGS_PRODUCT_ID = "ARGS_PRODUCT_ID"

    const val PARAM_IS_EDIT_MODE = "isEditMode"
    const val PARAM_FEEDBACK_ID = "feedbackId"
    const val PARAM_UTM_SOURCE = "utm_source"
    const val EDIT_MODE = true

    const val REPUTATION_SCORE_BAD = -1
    const val REPUTATION_SCORE_MEDIOCRE = 1
    const val REPUTATION_SCORE_EXCELLENT = 2

    const val REVIEW_PENDING_TRACE = "review_pending_trace"
    const val REVIEW_PENDING_PLT_PREPARE_METRICS = "review_pending_plt_prepare_metrics"
    const val REVIEW_PENDING_PLT_NETWORK_METRICS = "review_pending_plt_network_metrics"
    const val REVIEW_PENDING_PLT_RENDER_METRICS = "review_pending_plt_render_metrics"

    const val REVIEW_DETAIL_TRACE = "review_history_detail_trace"
    const val REVIEW_DETAIL_PLT_PREPARE_METRICS = "review_history_detail_plt_prepare_metrics"
    const val REVIEW_DETAIL_PLT_NETWORK_METRICS = "review_history_detail_plt_network_metrics"
    const val REVIEW_DETAIL_PLT_RENDER_METRICS = "review_history_detail_plt_render_metrics"

    const val CREATE_REVIEW_TRACE = "create_review_trace"
    const val CREATE_REVIEW_PLT_PREPARE_METRICS = "create_review_plt_prepare_metrics"
    const val CREATE_REVIEW_PLT_NETWORK_METRICS = "create_review_plt_network_metrics"
    const val CREATE_REVIEW_PLT_RENDER_METRICS = "create_review_plt_render_metrics"

    const val EDIT_REVIEW_TRACE = "edit_review_trace"
    const val EDIT_REVIEW_PLT_PREPARE_METRICS = "edit_review_plt_prepare_metrics"
    const val EDIT_REVIEW_PLT_NETWORK_METRICS = "edit_review_plt_network_metrics"
    const val EDIT_REVIEW_PLT_RENDER_METRICS = "edit_review_plt_render_metrics"

    const val RATING_PRODUCT_TRACE = "rating_product_trace"
    const val RATING_PRODUCT_PLT_PREPARE_METRICS = "rating_product_plt_prepare_metrics"
    const val RATING_PRODUCT_PLT_NETWORK_METRICS = "rating_product_plt_network_metrics"
    const val RATING_PRODUCT_PLT_RENDER_METRICS = "rating_product_plt_render_metrics"

    const val SELLER_REVIEW_DETAIL_TRACE = "seller_review_detail_trace"
    const val SELLER_REVIEW_DETAIL_PLT_PREPARE_METRICS = "seller_review_detail_plt_prepare_metrics"
    const val SELLER_REVIEW_DETAIL_PLT_NETWORK_METRICS = "seller_review_detail_plt_network_metrics"
    const val SELLER_REVIEW_DETAIL_PLT_RENDER_METRICS = "seller_review_detail_plt_render_metrics"

    const val SELLER_REVIEW_REPLY_TRACE = "seller_review_reply_trace"
    const val SELLER_REVIEW_REPLY_PLT_PREPARE_METRICS = "seller_review_reply_plt_prepare_metrics"
    const val SELLER_REVIEW_REPLY_PLT_NETWORK_METRICS = "seller_review_reply_plt_network_metrics"
    const val SELLER_REVIEW_REPLY_PLT_RENDER_METRICS = "seller_review_reply_plt_render_metrics"

    const val IV_MORE_FOCUS_SERVICE_PM = "https://images.tokopedia.net/img/android/shop_score/iv_more_focus_shop_service_pm@3x.png"
    const val IV_MORE_FOCUS_SERVICE_RM = "https://images.tokopedia.net/img/android/shop_score/iv_more_focus_shop_service_rm@3x.png"
    const val IV_MORE_INTEREST_BUYER = "https://images.tokopedia.net/img/android/shop_score/iv_more_interest_buyer@3x.png"
}


