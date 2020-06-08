package com.tokopedia.reviewseller.feature.reviewreply.view.model

data class InsertTemplateReplyUiModel(
        var isSuccess: Boolean = false,
        var templateId: Int? = 0,
        var title: String? = "",
        var message: String? = "",
        var status: Int? = 0,
        var defaultTemplateID: Int? = 0,
        var error: String? = ""
)