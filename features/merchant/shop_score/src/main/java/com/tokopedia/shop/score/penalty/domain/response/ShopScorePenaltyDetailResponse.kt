package com.tokopedia.shop.score.penalty.domain.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

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
            val result: List<Result> = listOf(),
            @SerializedName("startDate")
            val startDate: String = String.EMPTY,
            @SerializedName("endDate")
            val endDate: String = String.EMPTY,
            @SerializedName("defaultStartDate")
            val defaultStartDate: String = String.EMPTY,
            @SerializedName("defaultEndDate")
            val defaultEndDate: String = String.EMPTY
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
                val typeName: String = "",
                @SerializedName("penaltyTypeGroup")
                val penaltyTypeGroup: Int = Int.ZERO,
                @SerializedName("productDetail")
                val productDetail: ProductDetail = ProductDetail()
        ) {

            data class ProductDetail(
                @SerializedName("id")
                val id: String = String.EMPTY,
                @SerializedName("name")
                val name: String = String.EMPTY
            )

            companion object {
                const val PENALTY_TYPE_PRODUCT = 2
            }

        }

        data class Error(
                @Expose
                @SerializedName("message")
                val message: String = ""
        )
    }
}
