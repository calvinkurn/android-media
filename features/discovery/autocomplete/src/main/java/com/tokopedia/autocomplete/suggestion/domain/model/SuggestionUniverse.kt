package com.tokopedia.autocomplete.suggestion.domain.model

import com.google.gson.annotations.SerializedName

data class SuggestionUniverse(
        @SerializedName("data")
        val data: SuggestionData = SuggestionData(),

        @SerializedName("top_shops")
        val topShop: List<SuggestionTopShop> = listOf()
)