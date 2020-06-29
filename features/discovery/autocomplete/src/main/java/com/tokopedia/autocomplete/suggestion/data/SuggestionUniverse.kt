package com.tokopedia.autocomplete.suggestion.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.autocomplete.suggestion.SuggestionData
import com.tokopedia.autocomplete.suggestion.SuggestionTopShop

data class SuggestionUniverse(
        @SerializedName("data")
        val data: SuggestionData = SuggestionData(),

        @SerializedName("top_shops")
        val topShop: List<SuggestionTopShop> = listOf()
)