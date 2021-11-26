package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.SerializedName

data class SuggestionTracking(
        @SerializedName("code")
        val code: String = ""
)