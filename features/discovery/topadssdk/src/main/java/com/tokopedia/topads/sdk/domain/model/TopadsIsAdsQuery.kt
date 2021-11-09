package com.tokopedia.topads.sdk.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yfsx on 3/29/21.
 */
data class TopadsIsAdsQuery (
        @SerializedName("topAdsGetDynamicSlotting" )
        @Expose
        val data: TopAdsGetDynamicSlottingData = TopAdsGetDynamicSlottingData()
)

data class TopAdsGetDynamicSlottingData (
        @SerializedName("data")
        @Expose
        val productList: List<TopAdsGetDynamicSlottingDataProduct> = mutableListOf(),
        @SerializedName("status")
        @Expose
        val status: TopadsStatus = TopadsStatus()
)

data class TopAdsGetDynamicSlottingDataProduct (
        @SerializedName("product_click_url")
        @Expose
        val clickUrl: String = "",
        @SerializedName("is_charge")
        @Expose
        val isCharge: Boolean = false,
        @SerializedName("product")
        @Expose
        val product: TopadsProduct = TopadsProduct()
)

data class TopadsProduct(
        @SerializedName("image")
        @Expose
        val image :Image = Image(),
        @SerializedName("name")
        @Expose
        val name :String = "",
        @SerializedName("id")
        @Expose
        val id :String = ""
)
data class Image(
        @SerializedName("m_url")
        @Expose
        val m_url:String = "",
        @SerializedName("m_ecs")
        @Expose
        val m_ecs:String = ""
)

data class TopadsStatus(
        @SerializedName("error_code")
        @Expose
        val error_code:Int = 500,
        @SerializedName("message")
        @Expose
        val message:String = ""
)
