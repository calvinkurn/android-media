package com.tokopedia.developer_options.presentation.feedbackpage.domain.response

import com.google.gson.annotations.SerializedName

class CategoriesResponse (
        @SerializedName("categories")
        var categories: List<Category> = listOf()
)

data class Category(
        @SerializedName("label")
        var label: String = "",
        @SerializedName("value")
        var value: Int = 0
)