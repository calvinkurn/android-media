package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

data class KeywordActionResponse(

        @field:SerializedName("topAdsUpdateKeywords")
        val topAdsUpdateKeywords: TopAdsUpdateKeywords? = null
) {
    data class TopAdsUpdateKeywords(

            @field:SerializedName("data")
            val data: Data = Data(),

            @field:SerializedName("errors")
            val errors: List<ErrorsItem>? = listOf()
    ) {
        data class Data(
                @field:SerializedName("action")
                val action: String = "",

                @field:SerializedName("shopID")
                val shopID: String = ""
        )

    }
}
