package com.tokopedia.topads.credit.history.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class TopAdsCreditHistory(
        @SerializedName("credit_history")
        @Expose
        val creditHistory: List<CreditHistory> = listOf(),
        @SerializedName("total_addition")
        @Expose
        val totalAddition: Float = 0f,
        @SerializedName("total_addition_fmt")
        @Expose
        val totalAdditionFmt: String = "",
        @SerializedName("total_used")
        @Expose
        val totalUsed: Float = 0f,
        @SerializedName("total_used_fmt")
        @Expose
        val totalUsedFmt: String = ""
) {

    data class CreditsResponse(
            @SerializedName("topadsCreditHistory")
            @Expose
            val response: Response = Response()
    )

    data class Response (
            @SerializedName("data")
            @Expose
            val dataHistory: TopAdsCreditHistory = TopAdsCreditHistory(),

            @Expose
            @SerializedName("errors")
            val errors: List<Error> = listOf())
}