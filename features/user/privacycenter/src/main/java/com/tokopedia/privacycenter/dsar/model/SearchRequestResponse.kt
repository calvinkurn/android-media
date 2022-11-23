package com.tokopedia.privacycenter.dsar.model

import com.google.gson.annotations.SerializedName

data class SearchRequestResponse(
    @SerializedName("content")
    val results: List<SearchResult> = listOf()
)

data class SearchResult(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("email")
    val email: String = ""
)
