package com.tokopedia.topads.data.response
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
                val `data`: List<Data> = listOf(),
                @SerializedName("errors")
                val errors: List<Error> = listOf()
        ) {
            data class Data(
                    @SerializedName("suggestedBid")
                    val suggestedBid: Int = 0,
                    @SerializedName("productID")
                    val productID: Int = 0,
                    @SerializedName("groupID")
                    val groupID: Int = 0,
                    @SerializedName("priceBid")
                    val priceBid: Int = 0,
                    @SerializedName("productName")
                    val productName: String = "",
                    @SerializedName("productImage")
                    val productImage: String = "",
                    @SerializedName("adID")
                    val adID: Int = 0,
                    @SerializedName("productPrice")
                    val productPrice: String = ""
            )
        }
    }
}