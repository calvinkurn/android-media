package com.tokopedia.stories.creation.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on October 09, 2023
 */
data class GetStoryProductDetailsResponse(
    @SerializedName("contentCreatorStoryGetProductDetails")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("products")
        val products: List<Product> = emptyList()
    )

    data class Product(
        @SerializedName("productID")
        val productID: String = "",

        @SerializedName("productName")
        val productName: String = "",

        @SerializedName("imageURL")
        val imageURL: String = "",

        @SuppressLint("Invalid Data Type")
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
