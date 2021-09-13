package com.tokopedia.review.feature.inbox.buyerreview.network

import com.tokopedia.url.TokopediaUrl

object ReputationBaseURL {
    var BASE_DOMAIN: String = TokopediaUrl.getInstance().WS
    val URL_REPUTATION: String = BASE_DOMAIN + "reputationapp/"
    val PATH_SEND_REPUTATION_SMILEY: String = "reputation/api/v1/insert"
    val REPUTATIONAPP_REVIEW_API: String = "reputationapp/review/api/"
    private val REPUTATION_VERSION: String = "v1"
    val PATH_GET_INBOX_REPUTATION: String = ("reputation/api/"
            + REPUTATION_VERSION + "/inbox")
    val PATH_GET_DETAIL_INBOX_REPUTATION: String = ("review/api/"
            + REPUTATION_VERSION + "/list")
    val PATH_REPORT_REVIEW: String = ("review/api/"
            + REPUTATION_VERSION + "/report")
    val PATH_DELETE_REVIEW_RESPONSE: String = ("review/api/"
            + REPUTATION_VERSION + "/response/delete")
    val PATH_INSERT_REVIEW_RESPONSE: String = ("review/api/"
            + REPUTATION_VERSION + "/response/insert")
    val PATH_GET_LIKE_DISLIKE_REVIEW: String = ("review/api/"
            + REPUTATION_VERSION + "/likedislike")
    val PATH_LIKE_DISLIKE_REVIEW: String = ("review/api/"
            + REPUTATION_VERSION + "/likedislike")

    // Tome
    val PATH_IS_FAVORITE_SHOP: String = "v1/user/isfollowing"
    val PATH_SHOP: String = "v4/shop/"

    // upload
    val V4_ACTION_GENERATE_HOST: String = "v4/action/generate-host/"
}