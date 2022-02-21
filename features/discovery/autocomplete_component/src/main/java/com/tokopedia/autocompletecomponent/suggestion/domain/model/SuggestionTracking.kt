package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionTracking(
        @SerializedName("code")
        @Expose
        val code: String = ""
)