package com.tokopedia.feedback_form.feedbackpage.domain.response

import com.google.gson.annotations.SerializedName

class FeedbackDataResponse (
        @SerializedName("categories")
        var categories: List<Category> = listOf(),
        @SerializedName("labels")
        var labels: List<Labels> = listOf()
)

data class Category(
        @SerializedName("label")
        var label: String = "",
        @SerializedName("value")
        var value: Int = -1
)

data class Labels (
        @SerializedName("id")
        var idLabel: Int = -1,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("weight")
        var weight: String = ""
)