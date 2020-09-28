package com.tokopedia.developer_options.presentation.feedbackpage.domain.model

data class CategoriesModel(
        var categories: List<CategoriesItem> = emptyList()
)

data class CategoriesItem(
        var label: String = "",
        var value: Int = -1
)