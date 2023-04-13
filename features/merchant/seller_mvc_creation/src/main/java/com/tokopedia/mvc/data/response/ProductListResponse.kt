package com.tokopedia.mvc.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("ProductList")
    val productList: ProductList = ProductList()
) {
    data class ProductList(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("meta")
        val meta: Meta = Meta(0),
        @SerializedName("data")
        val `data`: List<Data> = listOf()
    ) {
        data class Header(
            @SerializedName("messages")
            val messages: List<String> = emptyList(),
        )
        data class Meta(
            @SerializedName("totalHits")
            val totalHits: Int = 0
        )
        data class Data(
            @SerializedName("id")
            val id: String = "",
            @SerializedName("isVariant")
            val isVariant: Boolean = false,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("pictures")
            val pictures: List<Picture> = listOf(),
            @SerializedName("preorder")
            val preorder: Preorder = Preorder(),
            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            val price: Price = Price(),
            @SerializedName("sku")
            val sku: String = "",
            @SerializedName("stats")
            val stats: Stats = Stats(),
            @SerializedName("status")
            val status: String = "",
            @SerializedName("stock")
            val stock: Int = 0,
            @SerializedName("txStats")
            val txStats: TxStats = TxStats(),
            @SerializedName("warehouse")
            val warehouse: List<Warehouse> = listOf(),
            @SerializedName("warehouseCount")
            val warehouseCount: Int = 0
        ) {
            data class Picture(
                @SerializedName("urlThumbnail")
                val urlThumbnail: String = ""
            )

            data class Preorder(
                @SerializedName("durationDays")
                val durationDays: Int = 0
            )

            data class Price(
                @SerializedName("max")
                val max: Int = 0,
                @SerializedName("min")
                val min: Int = 0
            )

            data class Stats(
                @SerializedName("countReview")
                val countReview: Int = 0,
                @SerializedName("countTalk")
                val countTalk: Int = 0,
                @SerializedName("countView")
                val countView: Int = 0
            )

            data class TxStats(
                @SerializedName("sold")
                val sold: Int = 0
            )

            data class Warehouse(
                @SerializedName("id")
                val id: String = ""
            )
        }
    }
}
