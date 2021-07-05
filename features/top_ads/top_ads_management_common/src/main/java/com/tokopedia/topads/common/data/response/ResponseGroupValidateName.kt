package com.tokopedia.topads.common.data.response


import com.google.gson.annotations.SerializedName

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