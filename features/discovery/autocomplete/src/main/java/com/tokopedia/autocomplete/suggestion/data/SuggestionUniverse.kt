package com.tokopedia.autocomplete.suggestion.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.autocomplete.suggestion.SuggestionData

data class SuggestionUniverse(
        @SerializedName("data")
        val data: SuggestionData = SuggestionData()
)