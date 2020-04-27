package com.tokopedia.topads.edit.data.response


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class ResponseGroupValidateName(
        @SerializedName("topAdsGroupValidateName")
        val topAdsGroupValidateName: TopAdsGroupValidateName = TopAdsGroupValidateName()
) {
    data class TopAdsGroupValidateName(
            @SerializedName("data")
            val `data`: Data = Data(),
            @SerializedName("errors")
            val errors: List<Error> = listOf()
    ) {
        data class Data(
                @SerializedName("groupName")
                val groupName: String = "",
                @SerializedName("shopID")
                val shopID: Int = 0
        )
    }
}