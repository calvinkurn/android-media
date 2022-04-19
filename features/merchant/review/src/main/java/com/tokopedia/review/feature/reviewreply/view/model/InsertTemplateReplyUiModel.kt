package com.tokopedia.review.feature.reviewreply.view.model

data class InsertTemplateReplyUiModel(
        var isSuccess: Boolean = false,
        var defaultTemplateID: Long? = 0,
        var error: String? = ""
)