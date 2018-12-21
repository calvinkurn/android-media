package com.tokopedia.topads.dashboard.data.model.credit_history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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

    data class Response(
            @SerializedName("data")
            @Expose
            val dataHistory: TopAdsCreditHistory = TopAdsCreditHistory(),

            @Expose
            @SerializedName("errors")
            val errors: List<CreditError> = listOf())

    data class CreditError(
            @SerializedName("code")
            @Expose
            val code: String = "",

            @SerializedName("detail")
            @Expose
            val detail: String = "",

            @SerializedName("title")
            @Expose
            val title: String = ""
    )
}