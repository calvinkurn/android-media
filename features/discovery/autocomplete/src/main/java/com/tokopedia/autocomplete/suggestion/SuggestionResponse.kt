package com.tokopedia.autocomplete.suggestion

import com.google.gson.annotations.SerializedName

data class SuggestionResponse (
    @SerializedName("process_time")
    val processTime: Double = 0.0,

    @SerializedName("data")
    val data: SuggestionData = SuggestionData()
)