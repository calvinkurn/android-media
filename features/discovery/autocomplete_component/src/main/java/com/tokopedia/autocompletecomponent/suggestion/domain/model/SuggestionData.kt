package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.SerializedName

data class SuggestionData(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val header: String = "",

        @SerializedName("items")
        val items: List<SuggestionItem> = listOf()
)