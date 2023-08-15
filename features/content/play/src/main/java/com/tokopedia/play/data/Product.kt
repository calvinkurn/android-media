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
        val discount: Long = 0,
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
        val webLink: String = "",
        @SerializedName("is_toko_now")
        val isTokoNow: Boolean = false,
        @SerializedName("is_pinned")
        val isPinned: Boolean = false,
        @SerializedName("available_buttons")
        val buttons: List<ProductButton> = emptyList(),
        @SerializedName("product_number")
        val number: Int = 0,
        @SerializedName("rating")
        val rating: String = "",
        @SerializedName("sold_quantity")
        val soldQuantity: String = "",
        @SerializedName("social_proof_rank")
        val rankFmt: String = "",
        @SerializedName("social_proof_raw_value")
        val rank: Int = 0,
        @SerializedName("social_proof_tag_color")
        val ribbonColors: List<String>? = emptyList(),
        @SerializedName("social_proof_type_value")
        val rankType: String = "",
){
    data class ProductButton(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("color")
        val color: String = "",
        @SerializedName("button_type")
        val buttonType: String = ""
    )
}
