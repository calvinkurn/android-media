package com.tokopedia.review.feature.reviewreply.view.model

data class UpdateReplyResponseUiModel (
        var isSuccess: Boolean = false,
        var feedbackId: String? = "",
        var responseBy: String? = "",
        var shopId: String? = "",
        var responseMessage: String? = ""
)