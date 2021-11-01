package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class WhiteListUserResponse(
    @field:SerializedName("topAdsGetShopWhitelistedFeature")
    var topAdsGetShopWhitelistedFeature: TopAdsGetShopWhitelistedFeature = TopAdsGetShopWhitelistedFeature()
) {
    data class TopAdsGetShopWhitelistedFeature(
        @field:SerializedName("data")
        val data: List<Data> = listOf(),

        @field:SerializedName("errors")
        var errors: List<Error> = listOf()

    ) {

        data class Data(
        @field:SerializedName("featureID")
        var featureId: Int = 0,

        @field:SerializedName("featureName")
        var featureName: String =""
        )
    }
}
