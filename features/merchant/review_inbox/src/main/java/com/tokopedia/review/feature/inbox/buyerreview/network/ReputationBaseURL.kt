package com.tokopedia.review.feature.inbox.buyerreview.network

import com.tokopedia.url.TokopediaUrl

object ReputationBaseURL {
    private var BASE_DOMAIN: String = TokopediaUrl.getInstance().WS
    val URL_REPUTATION: String = BASE_DOMAIN + "reputationapp/"
    const val PATH_SEND_REPUTATION_SMILEY: String = "reputation/api/v1/insert"
    private const val REPUTATION_VERSION: String = "v1"
    const val PATH_GET_INBOX_REPUTATION: String = ("reputation/api/"
            + REPUTATION_VERSION + "/inbox")
    const val PATH_GET_DETAIL_INBOX_REPUTATION: String = ("review/api/"
            + REPUTATION_VERSION + "/list")
    const val PATH_REPORT_REVIEW: String = ("review/api/"
            + REPUTATION_VERSION + "/report")
    const val PATH_DELETE_REVIEW_RESPONSE: String = ("review/api/"
            + REPUTATION_VERSION + "/response/delete")

    // Tome
    const val PATH_IS_FAVORITE_SHOP: String = "v1/user/isfollowing"

    // upload
    const val V4_ACTION_GENERATE_HOST: String = "v4/action/generate-host/"
}