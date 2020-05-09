package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductHighlightItem(
        @Expose
        @SerializedName("ace_search_product")
        var aceProduct: ProductHighlight = ProductHighlight()
)

data class ProductHighlight(
        @Expose
        @SerializedName("data")
        var data: ProductHighlightData = ProductHighlightData()
)

data class ProductHighlightData(
        @Expose
        @SerializedName("products")
        var products: List<HighlightProducts> = emptyList()
)

data class HighlightProducts(
        @Expose @SerializedName("id") var id: Int = 0,
        @Expose @SerializedName("name") var name: String = "",
        @Expose @SerializedName("imageURL") var imageURL: String = "",
        @Expose @SerializedName("price") var price: String = "",
        @Expose @SerializedName("isStockEmpty") var isStockEmpty: Boolean = false,
        @Expose @SerializedName("freeOngkir") var freeOngkir: ProductFreeOngkir? = null,
        @Expose @SerializedName("originalPrice") var originalPrice: String = "",
        @Expose @SerializedName("discountPercentage") var discountPercentage: Int = 0
)

data class ProductFreeOngkir(
        @Expose @SerializedName("imgURL") var imgURL: String = "",
        @Expose @SerializedName("isActive") var isActive: Boolean = false
)