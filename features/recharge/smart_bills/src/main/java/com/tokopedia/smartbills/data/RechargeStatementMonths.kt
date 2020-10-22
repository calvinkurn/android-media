package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeStatementMonths(
        @SerializedName("index")
        @Expose
        val index: Int = 0,
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("month")
        @Expose
        val month: Int = 0,
        @SerializedName("year")
        @Expose
        val year: Int = 0,
        @SerializedName("isOngoing")
        @Expose
        val isOngoing: Boolean = false
) {
    data class Response(
            @SerializedName("rechargeStatementMonths")
            @Expose
            val response: List<RechargeStatementMonths>? = null
    )
}