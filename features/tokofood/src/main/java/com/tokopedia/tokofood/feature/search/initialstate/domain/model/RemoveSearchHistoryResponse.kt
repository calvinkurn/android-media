package com.tokopedia.tokofood.feature.search.initialstate.domain.model


import com.google.gson.annotations.SerializedName

data class RemoveSearchHistoryResponse(
    @SerializedName("tokofoodRemoveSearchHistory")
    val tokofoodRemoveSearchHistory: TokofoodRemoveSearchHistory = TokofoodRemoveSearchHistory()
) {
    data class TokofoodRemoveSearchHistory(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("success")
        val success: Boolean = false
    )
}