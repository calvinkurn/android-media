package com.tokopedia.reviewseller.feature.reviewreply.view.model

data class ReplyTemplateUiModel(
        var message: String? = "",
        var status: Int? = 0,
        var templateId: Int? = 0,
        var title: String? = "",
        var isSelected: Boolean = false
)