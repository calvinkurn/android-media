package com.tokopedia.internal_review.view.model

/**
 * Created By @ilhamsuaib on 28/01/21
 */

data class SendReviewParam(
        val userId: String,
        val rating: Int,
        val feedback: String = "",
        val appVersion: String = "",
        val deviceModel: String = "",
        val osType: String = "",
        val osVersion: String = ""
)