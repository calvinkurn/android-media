package com.tokopedia.topads.common.data.response


import com.google.gson.annotations.SerializedName

data class SingleAdInFo(
        @SerializedName("topAdsGetPromo")
        val topAdsGetPromo: TopAdsGetPromo = TopAdsGetPromo()
)

data class TopAdsGetPromo(
        @SerializedName("data")
        val `data`: List<SingleAd> = listOf(),
        @SerializedName("errors")
        val errors: List<Any> = listOf()
)

data class SingleAd(
        @SerializedName("adType")
        val adType: String = "",
        @SerializedName("itemID")
        val itemID: String = "",
        @SerializedName("priceBid")
        val priceBid: Int = 0,
        @SerializedName("priceDaily")
        val priceDaily: Int = 0,
        @SerializedName("status")
        val status: String = ""
)