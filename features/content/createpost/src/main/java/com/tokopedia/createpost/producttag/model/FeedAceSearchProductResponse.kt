package com.tokopedia.createpost.producttag.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on April 28, 2022
 */
data class FeedAceSearchProductResponse(
    @SerializedName("ace_search_product_v4")
    val wrapper: Wrapper = Wrapper(),
) {
    data class Wrapper(
        @SerializedName("data")
        val data: Data = Data(),

        @SerializedName("suggestion")
        val suggestion: Suggestion = Suggestion(),
    )

    data class Data(
        @SerializedName("products")
        val products: List<Product> = emptyList(),
    )

    data class Product(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("shop")
        val shop: Shop = Shop(),

        @SerializedName("freeOngkir")
        val freeOngkir: FreeOngkir = FreeOngkir(),

        @SerializedName("url")
        val url: String = "",

        @SerializedName("imageUrl300")
        val imageUrl: String = "",

        @SerializedName("price")
        val price: String = "",

        @SerializedName("priceInt")
        val priceInt: Long = 0,

        @SerializedName("originalPrice")
        val originalPrice: String = "",

        @SerializedName("discountPercentage")
        val discountPercentage: Double = 0.0,

        @SerializedName("ratingAverage")
        val ratingAverage: String = "",

        @SerializedName("count_sold")
        val countSold: String = "",

        @SerializedName("stock")
        val stock: Long = 0,

        @SerializedName("badges")
        val badges: List<Badge> = emptyList(),
    )

    data class Shop(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("isOfficial")
        val isOfficial: Boolean = false,

        @SerializedName("isPowerBadge")
        val isPowerBadge: Boolean = false,
    )

    data class Badge(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("imageUrl")
        val imageUrl: String = "",

        @SerializedName("show")
        val show: Boolean = false,
    )

    data class FreeOngkir(
        @SerializedName("isActive")
        val isActive: Boolean = false,

        @SerializedName("imgUrl")
        val imgUrl: String = "",
    )

    data class Suggestion(
        @SerializedName("text")
        val text: String = "",
    )
}