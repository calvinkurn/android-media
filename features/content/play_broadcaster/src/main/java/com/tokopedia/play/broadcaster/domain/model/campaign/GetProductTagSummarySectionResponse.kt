package com.tokopedia.play.broadcaster.domain.model.campaign

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on February 08, 2022
 */
data class GetProductTagSummarySectionResponse(
    @SerializedName("sections")
    val section: List<ProductTagSection> = emptyList()
) {
    data class ProductTagSection(
        @SerializedName("name")
        val name: String = "",

        @SerializedName("statusFmt")
        val statusFmt: String = "",

        @SerializedName("products")
        val products: List<ProductList> = emptyList(),
    )

    data class ProductList(
        @SerializedName("productID")
        val productID: String = "",

        @SerializedName("productName")
        val productName: String = "",

        @SerializedName("imageURL")
        val imageURL: String = "",

        @SerializedName("price")
        val price: String = "",

        @SerializedName("priceFmt")
        val priceFmt: String = "",

        @SerializedName("discount")
        val discount: String = "",

        @SerializedName("originalPrice")
        val originalPrice: String = "",

        @SerializedName("originalPriceFmt")
        val originalPriceFmt: String = "",

        @SerializedName("quantity")
        val quantity: Int = 0,
    )
}