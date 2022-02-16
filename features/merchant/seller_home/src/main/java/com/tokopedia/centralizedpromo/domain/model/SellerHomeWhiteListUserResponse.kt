package com.tokopedia.centralizedpromo.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SellerHomeWhiteListUserResponse(
    @SerializedName("topAdsGetShopWhitelistedFeature")
    @Expose
    var topAdsGetShopWhitelistedFeature: TopAdsGetShopWhitelistedFeature = TopAdsGetShopWhitelistedFeature()
) {
    data class TopAdsGetShopWhitelistedFeature(
        @SerializedName("data")
        @Expose
        val data: List<Data> = listOf(),

        @SerializedName("errors")
        @Expose
        var errors: List<Error> = listOf()

    ) {

        data class Data(
            @SerializedName("featureID")
            @Expose
            var featureId: Int = 0,

            @SerializedName("featureName")
            @Expose
            var featureName: String = ""
        )
    }
}
