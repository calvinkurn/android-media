package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionChildItem(
        @SerializedName("template")
        @Expose
        val template: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("applink")
        @Expose
        val applink: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("title")
        @Expose
        val title: String = ""
)