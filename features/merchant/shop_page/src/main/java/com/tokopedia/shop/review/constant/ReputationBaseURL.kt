package com.tokopedia.shop.review.constant

import com.tokopedia.url.TokopediaUrl.Companion.getInstance

object ReputationBaseURL {
    var BASE_DOMAIN = getInstance().WS
    val URL_REPUTATION = BASE_DOMAIN + "reputationapp/"
    const val REPUTATIONAPP_REVIEW_API = "reputationapp/review/api/"
    private const val REPUTATION_VERSION = "v1"
    const val PATH_DELETE_REVIEW_RESPONSE = ("review/api/"
            + REPUTATION_VERSION + "/response/delete")
    const val PATH_GET_LIKE_DISLIKE_REVIEW = ("review/api/"
            + REPUTATION_VERSION + "/likedislike")
    const val PATH_LIKE_DISLIKE_REVIEW = ("review/api/"
            + REPUTATION_VERSION + "/likedislike")
    const val PATH_GET_REVIEW_SHOP_LIST = (REPUTATIONAPP_REVIEW_API
            + REPUTATION_VERSION + "/shop")
    const val PATH_SHOP = "v4/shop/"
    // product
    const val PATH_LIKE_DISLIKE_REVIEW_PRODUCT = "like_dislike_review.pl"
    const val PATH_REPORT_REVIEW_PRODUCT = "report_review.pl"
    val URL_REVIEW_ACTION = BASE_DOMAIN + "v4/action/review/"
}