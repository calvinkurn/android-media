package com.tokopedia.topads.auto.data.network.response

import com.google.gson.annotations.SerializedName

data class NonDeliveryResponse(

        @field:SerializedName("topAdsGetShopStatus")
        val topAdsGetShopStatus: TopAdsGetShopStatus = TopAdsGetShopStatus()

) {
    data class TopAdsGetShopStatus(

            @field:SerializedName("data")
            val data: List<DataItem> = listOf(),

            @field:SerializedName("errors")
            val errors: List<ErrorResponse> = listOf()
    ) {
        data class DataItem(

                @field:SerializedName("statusDesc")
                val statusDesc: String = "",

                @field:SerializedName("statusDetail")
                val statusDetail: String = "",

                @field:SerializedName("status")
                val status: Int = 0
        )
    }
}