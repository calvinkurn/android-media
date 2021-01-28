package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

data class TotalProductKeyResponse(

        @field:SerializedName("topAdsGetTotalAdsAndKeywords")
        val topAdsGetTotalAdsAndKeywords: TopAdsGetTotalAdsAndKeywords = TopAdsGetTotalAdsAndKeywords()
)

data class TopAdsGetTotalAdsAndKeywords(

        @field:SerializedName("data")
        val data: List<CountDataItem> = listOf(),

        @field:SerializedName("errors")
        val errors: List<ErrorsItem> = listOf()
)

data class CountDataItem(

        @field:SerializedName("ID")
        val iD: String = "",

        @field:SerializedName("totalAds")
        val totalAds: Int = 0,

        @field:SerializedName("totalProducts")
        val totalProducts: Int = 0,

        @field:SerializedName("totalKeywords")
        val totalKeywords: Int = 0
)