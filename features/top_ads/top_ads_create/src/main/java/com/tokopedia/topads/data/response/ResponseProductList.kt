package com.tokopedia.topads.data.response


import com.google.gson.annotations.SerializedName

data class ResponseProductList(
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
        @SerializedName("ad_id")
        val adId: Int = 0,
        @SerializedName("ad_status")
        val adStatus: Int = 0,
        @SerializedName("department_id")
        val departmentId: Int = 0,
        @SerializedName("department_name")
        val departmentName: String = "",
        @SerializedName("group_id")
        val groupId: Int = 0,
        @SerializedName("group_name")
        val groupName: String = "",
        @SerializedName("price_bid")
        val priceBid: Int = 0,
        @SerializedName("product_id")
        val productId: Int = 0,
        @SerializedName("product_image")
        val productImage: String = "",
        @SerializedName("product_is_promoted")
        val productIsPromoted: Boolean = false,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("product_price")
        val productPrice: String = "",
        @SerializedName("product_price_num")
        val productPriceNum: Int = 0,
        @SerializedName("product_uri")
        val productUri: String = "",
        @SerializedName("suggested_bid")
        val suggestedBid: Int = 0
    )
}