package com.tokopedia.review.feature.reviewreply.view.model

data class ReplyTemplateUiModel(
        var message: String? = "",
        var status: Long? = 0,
        var templateId: Long? = 0,
        var title: String? = "",
        var isSelected: Boolean = false
)