package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeStatementBills(
        @SerializedName("total")
        @Expose
        val total: Int = 0,
        @SerializedName("totalText")
        @Expose
        val totalText: String = "",
        @SerializedName("month")
        @Expose
        val month: Int = 0,
        @SerializedName("monthText")
        @Expose
        val monthText: String = "",
        @SerializedName("dateRangeText")
        @Expose
        val dateRangeText: String = "",
        @SerializedName("isOngoing")
        @Expose
        val isOngoing: Boolean = false,
        @SerializedName("summaries")
        @Expose
        val summaries: List<Summaries> = listOf(),
        @SerializedName("bills")
        @Expose
        val bills: List<RechargeBills> = listOf()
) {
    data class Response(
            @SerializedName("rechargeStatementBills")
            @Expose
            val response: RechargeStatementBills? = null
    )

    data class Summaries(
            @SerializedName("categoryID")
            @Expose
            val categoryID: Int = 0,
            @SerializedName("categoryName")
            @Expose
            val categoryName: String = "",
            @SerializedName("total")
            @Expose
            val total: Float = 0f,
            @SerializedName("totalText")
            @Expose
            val totalText: String = "",
            @SerializedName("percentage")
            @Expose
            val percentage: Float = 0f
    )
}