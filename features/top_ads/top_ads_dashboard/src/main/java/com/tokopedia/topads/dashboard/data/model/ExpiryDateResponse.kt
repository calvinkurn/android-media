package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

data class ExpiryDateResponse(

    @field:SerializedName("topAdsGetFreeDeposit")
    val topAdsGetFreeDeposit: TopAdsGetFreeDeposit = TopAdsGetFreeDeposit(),
) {
    data class TopAdsGetFreeDeposit(

        @SerializedName("expiryDate")
        val expiryDate: String = "",

        @field:SerializedName("depositID")
        val depositID: Int = 0,

        @field:SerializedName("nominal")
        val nominal: Float = 0f,

        @field:SerializedName("status")
        val status: Int = 0,

        @field:SerializedName("pendingRebateCredit")
        val pendingRebateCredit: String? = "",
    )
}