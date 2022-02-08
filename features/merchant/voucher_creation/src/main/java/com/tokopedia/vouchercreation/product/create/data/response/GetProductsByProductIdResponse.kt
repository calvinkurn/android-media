package com.tokopedia.vouchercreation.product.create.data.response

import com.google.gson.annotations.SerializedName

data class GetProductsByProductIdResponse(
    @SerializedName("ProductList")
    val productList: GetProductListData
) {

    data class GetProductListData(

        @SerializedName("header")
        val header: Header = Header(),

        @SerializedName("data")
        val data: List<Data> = emptyList(),

        @SerializedName("meta")
        val meta: Meta = Meta()
    )

    data class Header(

        @SerializedName("messages")
        val messages: List<String> = emptyList(),

        @SerializedName("reason")
        val reason: String = "",

        @SerializedName("errorCode")
        val errorCode: String = ""
    )

    data class Data(

        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("pictures")
        val pictures: List<Picture> = emptyList(),

        @SerializedName("txStats")
        val txStats: TxStats = TxStats()
    )

    data class TxStats(
        @SerializedName("sold")
        val sold: Int = 0
    )

    data class Picture(

        @SerializedName("urlThumbnail")
        val urlThumbnail: String = ""
    )

    data class Meta(

        @SerializedName("totalHits")
        val totalHits: Int = 0
    )
}