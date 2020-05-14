package com.tokopedia.autocomplete.suggestion

import com.google.gson.annotations.SerializedName

data class SuggestionTracking(
        @SerializedName("code")
        val code: String = ""
)