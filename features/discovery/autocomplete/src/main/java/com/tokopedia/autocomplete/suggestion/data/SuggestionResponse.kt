package com.tokopedia.autocomplete.suggestion.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.autocomplete.suggestion.SuggestionData

data class SuggestionResponse (
    @SerializedName("process_time")
    val processTime: Double = 0.0,

    @SerializedName("data")
    val data: SuggestionData = SuggestionData()
)