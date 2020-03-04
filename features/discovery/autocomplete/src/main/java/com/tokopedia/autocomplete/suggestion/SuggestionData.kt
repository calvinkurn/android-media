package com.tokopedia.autocomplete.suggestion

import com.google.gson.annotations.SerializedName

data class SuggestionData(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val header: String = "",

        @SerializedName("items")
        val items: List<SuggestionItem> = listOf()
) {
    companion object {
        const val SUGGESTION_HEADER = "header"
        const val SUGGESTION_SINGLE_LINE = "list_single_line"
        const val SUGGESTION_DOUBLE_LINE = "list_double_line"
        const val TYPE_KEYWORD = "keyword"
        const val TYPE_RECENT_KEYWORD = "recent keyword"
        const val TYPE_CURATED = "curated"
        const val TYPE_SHOP = "shop"
        const val TYPE_PROFILE = "profile"
    }
}