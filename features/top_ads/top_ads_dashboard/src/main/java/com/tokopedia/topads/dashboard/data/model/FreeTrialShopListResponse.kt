package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 22/4/20.
 */


data class FreeTrialShopListResponse(

        @field:SerializedName("topAdsGetShopWhitelistedFeature")
        val topAdsGetShopWhitelistedFeature: TopAdsGetShopWhitelistedFeature = TopAdsGetShopWhitelistedFeature()
) {
    data class TopAdsGetShopWhitelistedFeature(
            @field:SerializedName("data")
            val data: List<DataItem?> = listOf(),

            @field:SerializedName("errors")
            val errors: List<Error>? = listOf()
    ) {
        data class DataItem(

                @field:SerializedName("featureName")
                val fetaureName: String? = null,

                @field:SerializedName("featureID")
                val featureID: Int? = null
        ) {
            data class Error(
                    @SerializedName("code")
                    val code: String = "",
                    @SerializedName("detail")
                    val detail: String = "",
                    @SerializedName("object")
                    val objectX: Object = Object(),
                    @SerializedName("title")
                    val title: String = ""
            ) {
                data class Object(
                        @SerializedName("text")
                        val text: Any? = Any(),
                        @SerializedName("type")
                        val type: Int = 0
                )
            }
        }
    }
}