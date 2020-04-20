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
                val `data`: List<Data> = listOf(),
                @SerializedName("eof")
                val eof: Boolean = false,
                @SerializedName("errors")
                val errors: List<Error> = listOf(),
                @SerializedName("meta")
                val meta: Meta = Meta()
        ) {
            data class Data(
                    @SerializedName("departmentName")
                    val departmentName: String = "",
                    @SerializedName("productURI")
                    val productURI: String = "",
                    @SerializedName("suggestedBid")
                    val suggestedBid: Int = 0,
                    @SerializedName("productID")
                    val productID: Int = 0,
                    @SerializedName("adStatus")
                    val adStatus: Int = 0,
                    @SerializedName("groupID")
                    val groupID: Int = 0,
                    @SerializedName("departmentID")
                    val departmentID: Int = 0,
                    @SerializedName("productReviewCount")
                    val productReviewCount: Int = 0,
                    @SerializedName("priceBid")
                    val priceBid: Int = 0,
                    @SerializedName("productName")
                    val productName: String = "",
                    @SerializedName("productIsPromoted")
                    val productIsPromoted: Boolean = false,
                    @SerializedName("groupName")
                    val groupName: String = "",
                    @SerializedName("productImage")
                    val productImage: String = "",
                    @SerializedName("adID")
                    val adID: Int = 0,
                    @SerializedName("productPriceNum")
                    val productPriceNum: Int = 0,
                    @SerializedName("productRating")
                    val productRating: Int = 0,
                    @SerializedName("productPrice")
                    val productPrice: String = ""
            )
        }
    }
}