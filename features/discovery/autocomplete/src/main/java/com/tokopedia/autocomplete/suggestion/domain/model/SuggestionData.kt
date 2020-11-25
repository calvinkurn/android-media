package com.tokopedia.autocomplete.suggestion.domain.model

import com.google.gson.annotations.SerializedName

data class SuggestionData(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val header: String = "",

        @SerializedName("items")
        val items: List<SuggestionItem> = listOf()
)