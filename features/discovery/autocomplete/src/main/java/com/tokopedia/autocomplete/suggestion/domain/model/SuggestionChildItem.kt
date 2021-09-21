package com.tokopedia.autocomplete.suggestion.domain.model

import com.google.gson.annotations.SerializedName

data class SuggestionChildItem(
        @SerializedName("template")
        val template: String = "",

        @SerializedName("type")
        val type: String = "",

        @SerializedName("applink")
        val applink: String = "",

        @SerializedName("url")
        val url: String = "",

        @SerializedName("title")
        val title: String = ""
)