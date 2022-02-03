package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created by mzennis on 2020-03-06.
 */
data class Product(
        @SerializedName("ID")
        val id: String = "",
        @SerializedName("Name")
        val name: String = "",
        @SerializedName("ImageUrl")
        val image: String = "",
        @SerializedName("ShopID")
        val shopId: String = "",
        @SerializedName("OriginalPrice")
        val originalPrice: Double = 0.0,
        @SerializedName("OriginalPriceFormatted")
        val originalPriceFormatted: String = "",
        @SerializedName("Price")
        val price: Double = 0.0,
        @SerializedName("PriceFormatted")
        val priceFormatted: String = "",
        @SerializedName("Discount")
        val discount: Int = 0,
        @SerializedName("Order")
        val order: Int = 0,
        @SerializedName("IsVariant")
        val isVariant: Boolean,
        @SerializedName("IsAvailable")
        val isAvailable: Boolean,
        @SerializedName("Quantity")
        val quantity: Int = 0,
        @SerializedName("MinQuantity")
        val minimumQuantity: Int = 0,
        @SerializedName("IsFreeShipping")
        val isFreeShipping: Boolean = false,
        @SerializedName("AppLink")
        val appLink: String = "",
        @SerializedName("WebLink")
        val webLink: String = ""
)