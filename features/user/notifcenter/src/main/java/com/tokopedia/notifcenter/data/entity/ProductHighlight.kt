package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductHighlightItem(
        @Expose
        @SerializedName("ace_search_product")
        val aceProduct: ProductHighlight = ProductHighlight()
)

data class ProductHighlight(
        @Expose
        @SerializedName("data")
        val data: ProductHighlightData = ProductHighlightData()
)

data class ProductHighlightData(
        @Expose
        @SerializedName("products")
        val products: List<HighlightProducts> = emptyList()
)

data class HighlightProducts(
        @Expose @SerializedName("id") val id: Int = 0,
        @Expose @SerializedName("name") val name: String = "",
        @Expose @SerializedName("imageURL") val imageURL: String = "",
        @Expose @SerializedName("price") val price: String = "",
        @Expose @SerializedName("priceInt") val priceInt: String = "",
        @Expose @SerializedName("isStockEmpty") val isStockEmpty: Boolean = false,
        @Expose @SerializedName("freeOngkir") val freeOngkir: ProductFreeOngkir? = null,
        @Expose @SerializedName("originalPrice") val originalPrice: String = "",
        @Expose @SerializedName("discountPercentage") val discountPercentage: Int = 0,
        @Expose @SerializedName("shop") val shop: Shop? = null
)

data class ProductFreeOngkir(
        @Expose @SerializedName("imgURL") val imgURL: String = "",
        @Expose @SerializedName("isActive") val isActive: Boolean = false
)