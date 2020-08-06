package com.tokopedia.reviewseller.feature.reviewreply.view.model

data class InsertTemplateReplyUiModel(
        var isSuccess: Boolean = false,
        var defaultTemplateID: Int? = 0,
        var error: String? = ""
)