package com.tokopedia.topads.common.data.response


import com.google.gson.annotations.SerializedName

data class ResponseGroupValidateName(
        @SerializedName("topAdsGroupValidateNameV2")
        val topAdsGroupValidateName: TopAdsGroupValidateNameV2 = TopAdsGroupValidateNameV2()
) {
    data class TopAdsGroupValidateNameV2(
            @SerializedName("data")
            val `data`: Data = Data(),
            @SerializedName("errors")
            val errors: List<Error> = listOf()
    ) {
        data class Data(
                @SerializedName("groupName")
                val groupName: String = "",
                @SerializedName("shopID")
                val shopID: String = "0"
        )
    }
}