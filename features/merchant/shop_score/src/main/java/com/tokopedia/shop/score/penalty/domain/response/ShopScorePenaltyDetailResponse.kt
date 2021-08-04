package com.tokopedia.shop.score.penalty.domain.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScorePenaltyDetailResponse(
        @Expose
        @SerializedName("shopScorePenaltyDetail")
        val shopScorePenaltyDetail: ShopScorePenaltyDetail = ShopScorePenaltyDetail()
) {
    data class ShopScorePenaltyDetail(
            @Expose
            @SerializedName("error")
            val error: Error = Error(),
            @Expose
            @SerializedName("hasNext")
            val hasNext: Boolean = false,
            @Expose
            @SerializedName("hasPrev")
            val hasPrev: Boolean = false,
            @Expose
            @SerializedName("result")
            val result: List<Result> = listOf()
    ) {
        data class Result(
                @Expose
                @SerializedName("createTime")
                val createTime: String = "",
                @Expose
                @SerializedName("invoiceNumber")
                val invoiceNumber: String = "",
                @Expose
                @SerializedName("penaltyExpirationDate")
                val penaltyExpirationDate: String = "",
                @Expose
                @SerializedName("penaltyStartDate")
                val penaltyStartDate: String = "",
                @Expose
                @SerializedName("reason")
                val reason: String = "",
                @Expose
                @SerializedName("score")
                val score: Int = 0,
                @Expose
                @SerializedName("shopPenaltyID")
                val shopPenaltyID: String = "",
                @Expose
                @SerializedName("status")
                val status: String = "",
                @Expose
                @SerializedName("typeID")
                val typeID: Int = 0,
                @Expose
                @SerializedName("typeName")
                val typeName: String = ""
        )

        data class Error(
                @Expose
                @SerializedName("message")
                val message: String = ""
        )
    }
}