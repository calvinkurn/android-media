package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchResultUiModel

data class Contact(
        @SerializedName("data")
        val searchResults: List<SearchResultUiModel> = listOf(),
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @SerializedName("count")
        val count: String = ""
)