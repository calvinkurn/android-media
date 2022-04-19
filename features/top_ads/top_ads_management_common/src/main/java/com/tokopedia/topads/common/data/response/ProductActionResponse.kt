package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class ProductActionResponse(

        @field:SerializedName("topadsUpdateSingleAds")
        val topadsUpdateSingleAds: TopadsUpdateSingleAds = TopadsUpdateSingleAds()
) {
    data class TopadsUpdateSingleAds(

            @field:SerializedName("data")
            val data: Data = Data(),

            @field:SerializedName("errors")
            val errors: List<Error> = listOf()
    ) {
        data class Data(

                @field:SerializedName("action")
                val action: String = "",

                @field:SerializedName("shopID")
                val shopID: String = ""
        )
    }
}