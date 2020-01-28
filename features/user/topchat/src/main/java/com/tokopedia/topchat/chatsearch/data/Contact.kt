package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName

data class Contact(
        @SerializedName("data")
        val searchResults: List<SearchResult> = listOf(),
        @SerializedName("hasNext")
        val hasNext: Boolean = false
)