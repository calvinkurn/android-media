package com.tokopedia.mvc.data.response


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ProductV3Response(
    @SerializedName("getProductV3")
    val getProductV3: GetProductV3 = GetProductV3()
) {
    data class GetProductV3(
        @SerializedName("stats")
        val stats: Stats = Stats(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("productName")
        val productName: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("pictures")
        val pictures: List<Picture> = emptyList(),
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("txStats")
        val txStats: TxStats = TxStats(),
        @SerializedName("variant")
        val variant: Variant = Variant()
    ) {
        data class Picture(
            @SerializedName("urlThumbnail")
            val urlThumbnail: String = "",
        )
        data class Stats(
            @SerializedName("countReview")
            val countReview: Int = 0,
            @SerializedName("countTalk")
            val countTalk: Int = 0,
            @SerializedName("countView")
            val countView: Int = 0,
            @SerializedName("rating")
            val rating: Int = 0
        )

        data class TxStats(
            @SerializedName("itemSold")
            val itemSold: Int = 0,
            @SerializedName("txReject")
            val txReject: Int = 0,
            @SerializedName("txSuccess")
            val txSuccess: Int = 0
        )

        data class Variant(
            @SerializedName("products")
            val products: List<Product> = listOf(),
            @SerializedName("selections")
            val selections: List<Selection> = listOf()
        ) {
            data class Product(
                @SerializedName("combination")
                val combination: List<Int> = listOf(),
                @SerializedName("hasInbound")
                val hasInbound: Any = Any(),
                @SerializedName("isPrimary")
                val isPrimary: Boolean = false,
                @SerializedName("price")
                val price: String = "",
                @SerializedName("productID")
                val productID: String = "",
                @SerializedName("sku")
                val sku: String = "",
                @SerializedName("status")
                val status: String = "",
                @SerializedName("stock")
                val stock: Int = 0,
                @SerializedName("warehouseCount")
                val warehouseCount: Int = 0,
                @SuppressLint("Invalid Data Type")
                @SerializedName("warehouseIDAll")
                val warehouseIDAll: List<String> = listOf()
            )

            data class Selection(
                @SerializedName("identifier")
                val identifier: String = "",
                @SerializedName("options")
                val options: List<Option> = listOf(),
                @SerializedName("unitID")
                val unitID: String = "",
                @SerializedName("unitName")
                val unitName: String = "",
                @SerializedName("variantID")
                val variantID: String = "",
                @SerializedName("variantName")
                val variantName: String = ""
            ) {
                data class Option(
                    @SerializedName("hexCode")
                    val hexCode: String = "",
                    @SerializedName("unitValueID")
                    val unitValueID: String = "",
                    @SerializedName("value")
                    val value: String = ""
                )
            }
        }
    }
}
