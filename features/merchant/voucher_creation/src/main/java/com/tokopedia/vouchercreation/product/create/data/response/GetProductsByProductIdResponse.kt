package com.tokopedia.vouchercreation.product.create.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetProductsByProductIdResponse(
    @SerializedName("ProductList")
    @Expose val productList: GetProductListData
) {

    data class GetProductListData(

        @SerializedName("header")
        @Expose val header: Header = Header(),

        @SerializedName("data")
        @Expose val data: List<Data> = emptyList(),

        @SerializedName("meta")
        @Expose val meta: Meta = Meta()
    )

    data class Header(

        @SerializedName("messages")
        @Expose val messages: List<String> = emptyList(),

        @SerializedName("reason")
        @Expose val reason: String = "",

        @SerializedName("errorCode")
        @Expose val errorCode: String = ""
    )

    data class Data(

        @SerializedName("id")
        @Expose val id: String = "",

        @SerializedName("name")
        @Expose val name: String = "",

        @SerializedName("pictures")
        @Expose val pictures: List<Picture> = emptyList(),

        @SerializedName("txStats")
        @Expose val txStats: TxStats = TxStats(),

        @SerializedName("warehouse")
        @Expose val warehouse: GoodsWarehouse
    )

    data class TxStats(
        @SerializedName("sold")
        @Expose val sold: Int = 0
    )

    data class Picture(

        @SerializedName("urlThumbnail")
        @Expose val urlThumbnail: String = ""
    )

    data class Meta(

        @SerializedName("totalHits")
        @Expose val totalHits: Int = 0
    )

    data class GoodsWarehouse(
            @SerializedName("id")
            @Expose val id: String
    )
}