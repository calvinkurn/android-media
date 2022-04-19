package com.tokopedia.feedback_form.feedbackpage.domain.model

data class FeedbackModel(
        var categories: List<CategoriesItem> = emptyList(),
        var labels: List<LabelsItem> = emptyList()
)

data class CategoriesItem(
        var label: String = "",
        var value: Int = -1
)

data class LabelsItem(
        var id: Int = -1,
        var name: String = "",
        var weight: String = "",
        var isSelected: Boolean = false
)