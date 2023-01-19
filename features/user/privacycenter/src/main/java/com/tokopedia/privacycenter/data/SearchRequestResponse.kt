package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class SearchRequestResponse(
    @SerializedName("content")
    val results: List<SearchResult> = listOf()
)

data class SearchResult(
    @SerializedName("requestQueueRefId")
    val requestQueueRefId: String = "",
    @SerializedName("requestQueueId")
    val requestQueueId: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("deadline")
    val deadline: String = ""
)
