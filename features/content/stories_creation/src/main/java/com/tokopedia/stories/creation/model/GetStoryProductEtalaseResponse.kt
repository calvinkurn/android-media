package com.tokopedia.stories.creation.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
data class GetStoryProductEtalaseResponse(
    @SerializedName("contentCreatorStoryGetProductList")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("products")
        val products: List<Product> = emptyList(),

        @SerializedName("pagerCursor")
        val pagerCursor: PagerCursor = PagerCursor(),
    )

    data class Product(
        @SerializedName("ID")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("stock")
        val stock: Int = 0,

        @SerializedName("pictures")
        val pictures: List<Picture> = emptyList(),

        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Price = Price(),
    )

    data class Picture(
        @SerializedName("urlThumbnail")
        val urlThumbnail: String = ""
    )

    data class Price(
        @SerializedName("min")
        val min: Double? = 0.0,

        @SerializedName("max")
        val max: Double? = 0.0,
    )

    data class PagerCursor(
        @SerializedName("limit")
        val limit: Int = 0,

        @SerializedName("cursor")
        val cursor: String = "",

        @SerializedName("hasNext")
        val hasNext: Boolean = false,
    )
}
