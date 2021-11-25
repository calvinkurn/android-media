package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionUniverse(
        @SerializedName("data")
        @Expose
        val data: SuggestionData = SuggestionData(),

        @SerializedName("top_shops")
        @Expose
        val topShop: List<SuggestionTopShop> = listOf()
)