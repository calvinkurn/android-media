package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class ResponseProductList(
        @SerializedName("data")
        val result: Result = Result()
) {
    data class Result(
            @SerializedName("topadsGetListProduct")
            val topadsGetListProduct: TopadsGetListProduct = TopadsGetListProduct()
    ) {
        data class TopadsGetListProduct(
                @SerializedName("data")
                val `data`: List<TopAdsProductModel> = listOf(),
                @SerializedName("eof")
                val eof: Boolean = false,
                @SerializedName("errors")
                val errors: List<Error> = listOf()
        )
    }
}