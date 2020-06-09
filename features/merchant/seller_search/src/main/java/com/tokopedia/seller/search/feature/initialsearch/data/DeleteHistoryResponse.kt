package com.tokopedia.seller.search.feature.initialsearch.data

import com.google.gson.annotations.SerializedName

data class DeleteHistoryResponse(
        @SerializedName("message")
        val message: String? = "",
        @SerializedName("status")
        val status: String? = ""
)