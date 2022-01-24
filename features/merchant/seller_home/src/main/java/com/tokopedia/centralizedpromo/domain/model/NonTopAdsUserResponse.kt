package com.tokopedia.centralizedpromo.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NonTopAdsUserResponse(
    @SerializedName("topadsGetShopInfoV2_1")
    @Expose
    val topAdsGetShopInfoV21: TopAdsGetShopInfoV21? = null
) {
    data class TopAdsGetShopInfoV21(
        @SerializedName("data")
        @Expose
        val nonTopAdsUserData: NonTopAdsUserData? = null,
        @SerializedName("errors")
        @Expose
        val errors: List<Any?>? = null
    ) {
        data class NonTopAdsUserData(
            @SerializedName("ads")
            @Expose
            val ads: List<Ad?>? = null
        ) {
            data class Ad(
                @SerializedName("is_used")
                @Expose
                val isUsed: Boolean = false,
            )
        }
    }
}
