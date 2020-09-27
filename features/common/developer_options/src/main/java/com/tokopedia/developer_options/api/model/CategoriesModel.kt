package com.tokopedia.developer_options.api.model

data class CategoriesModel(
        var categories: List<CategoriesItem> = emptyList()
)

data class CategoriesItem(
        var label: String = "",
        var value: Int = -1
)