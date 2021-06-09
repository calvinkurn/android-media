package com.tokopedia.review.feature.reviewreply.view.model

data class UpdateReplyResponseUiModel (
        var isSuccess: Boolean = false,
        var feedbackId: Long? = 0,
        var responseBy: Int? = 0,
        var shopId: Long? = 0,
        var responseMessage: String? = ""
)