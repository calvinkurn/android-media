package com.tokopedia.topads.detail_sheet.data


import com.google.gson.annotations.SerializedName

data class AdInfo(
        @SerializedName("topAdsGetPromo")
        val topAdsGetPromo: TopAdsGetPromo = TopAdsGetPromo()
)

data class TopAdsGetPromo(
        @SerializedName("data")
        val `data`: List<AdData> = listOf(),
        @SerializedName("errors")
        val errors: List<Any> = listOf()
)

data class AdData(
        @SerializedName("adID")
        val adID: String = "",
        @SerializedName("adImage")
        val adImage: String = "",
        @SerializedName("adStartDate")
        val adStartDate: String = "",
        @SerializedName("adStartTime")
        val adStartTime: String = "",
        @SerializedName("adTitle")
        val adTitle: String = "",
        @SerializedName("adType")
        val adType: String = "",
        @SerializedName("groupID")
        val groupID: String = "",
        @SerializedName("itemID")
        val itemID: String = "",
        @SerializedName("priceBid")
        val priceBid: Int = 0,
        @SerializedName("priceDaily")
        val priceDaily: Int = 0,
        @SerializedName("shopID")
        val shopID: String = "",
        @SerializedName("status")
        val status: String = ""
)