package com.tokopedia.topads.sdk.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by yfsx on 3/29/21.
 */
data class TopadsIsAdsQuery (
        @SerializedName("topAdsGetDynamicSlotting" )
        val data: TopAdsGetDynamicSlottingData = TopAdsGetDynamicSlottingData()
)

data class TopAdsGetDynamicSlottingData (
        @SerializedName("data")
        val productList: List<TopAdsGetDynamicSlottingDataProduct> = mutableListOf(),
        @SerializedName("status")
        val status: TopadsStatus = TopadsStatus()
)

data class TopAdsGetDynamicSlottingDataProduct (
        @SerializedName("product_click_url")
        val clickUrl: String = "",
        @SerializedName("is_charge")
        val isCharge: Boolean = false,
        @SerializedName("product")
        val product: TopadsProduct = TopadsProduct()
)

data class TopadsProduct(
        @SerializedName("image")
        val image :Image = Image()
)
data class Image(
        @SerializedName("m_url")
        val m_url:String = ""
)

data class TopadsStatus(
        @SerializedName("error_code")
        val error_code:Int = 500,
        @SerializedName("message")
        val message:String = ""
)
