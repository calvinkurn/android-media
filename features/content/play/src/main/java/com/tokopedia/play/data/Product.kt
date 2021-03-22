package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created by mzennis on 2020-03-06.
 */
data class Product(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("image_url")
        val image: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("original_price")
        val originalPrice: Double = 0.0,
        @SerializedName("original_price_formatted")
        val originalPriceFormatted: String = "",
        @SerializedName("price")
        val price: Double = 0.0,
        @SerializedName("price_formatted")
        val priceFormatted: String = "",
        @SerializedName("discount")
        val discount: Int = 0,
        @SerializedName("order")
        val order: Int = 0,
        @SerializedName("is_variant")
        val isVariant: Boolean,
        @SerializedName("is_available")
        val isAvailable: Boolean,
        @SerializedName("quantity")
        val quantity: Int = 0,
        @SerializedName("min_quantity")
        val minimumQuantity: Int = 0,
        @SerializedName("is_free_shipping")
        val isFreeShipping: Boolean = false,
        @SerializedName("app_link")
        val appLink: String = "",
        @SerializedName("web_link")
        val webLink: String = ""
)