package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

data class ExpiryDateResponse(

        @field:SerializedName("topAdsGetFreeDeposit")
        val topAdsGetFreeDeposit: TopAdsGetFreeDeposit = TopAdsGetFreeDeposit()
) {
    data class TopAdsGetFreeDeposit(

            @field:SerializedName("expiryDate")
            val expiryDate: String = "",

            @field:SerializedName("depositID")
            val depositID: Int = 0,

            @field:SerializedName("nominal")
            val nominal: Int = 0,

            @field:SerializedName("status")
            val status: Int = 0
    )
}