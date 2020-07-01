package com.tokopedia.seller.search.feature.initialsearch.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteHistoryResponse(
        @Expose
        @SerializedName("deleteHistory")
        val deleteHistory: DeleteHistory = DeleteHistory()
) {
    data class DeleteHistory(
            @Expose
            @SerializedName("message")
            val message: String? = "",
            @Expose
            @SerializedName("status")
            val status: String? = ""
    )
}