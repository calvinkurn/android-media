package com.tokopedia.sellerreview.view.model

/**
 * Created By @ilhamsuaib on 28/01/21
 */

data class SendReviewParam(
        val userId: Long,
        val rating: Int,
        val feedback: String = "",
        val appVersion: String = "",
        val deviceModel: String = "",
        val osType: String = "",
        val osVersion: String = ""
)