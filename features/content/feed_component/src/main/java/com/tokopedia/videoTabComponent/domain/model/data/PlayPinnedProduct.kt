package com.tokopedia.videoTabComponent.domain.model.data

import com.google.gson.annotations.SerializedName

data class PlayPinnedProduct(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("original_price")
        var original_price: String = "",
        @SerializedName("original_price_fmt")
        var original_price_fmt: String = "",
        @SerializedName("price")
        var price: String = "",
        @SerializedName("price_fmt")
        var price_fmt: String = "",
        @SerializedName("discount")
        var discount: Int = 0,
        @SerializedName("quantity")
        var quantity: Int = 0,
        @SerializedName("has_variant")
        var has_variant: Boolean = false,
        @SerializedName("is_available")
        var is_available: Boolean = false,
        @SerializedName("order")
        var order: Int = 0,
        @SerializedName("app_link")
        var appLink: String = "",
        @SerializedName("web_link")
        var webLink: String = "",
        @SerializedName("min_quantity")
        var min_quantity: Int = 0,
        @SerializedName("is_free_shipping")
        var is_free_shipping: Boolean = false,
        @SerializedName("image_url")
        var image_url: String = "",


)
