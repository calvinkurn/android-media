package com.tokopedia.sellerfeedback.presentation

data class SellerFeedback(
        var shopId: Int = 0,
        val feedbackScore: String,
        val feedbackType: String,
        val feedbackPage: String,
        val feedbackDetail: String,
        var uploadId1: String? = null,
        var uploadId2: String? = null,
        var uploadId3: String? = null
)
