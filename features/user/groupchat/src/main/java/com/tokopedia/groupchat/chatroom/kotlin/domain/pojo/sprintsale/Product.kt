package com.tokopedia.groupchat.chatroom.kotlin.domain.pojo.sprintsale

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product {

    @SerializedName("product_id")
    @Expose
    val productId: String? = null
    @SerializedName("name")
    @Expose
    val name: String? = null
    @SerializedName("url_mobile")
    @Expose
    val urlMobile: String? = null
    @SerializedName("image_url")
    @Expose
    val imageUrl: String? = null
    @SerializedName("discount_percentage")
    @Expose
    val discountPercentage: Int = 0
    @SerializedName("discounted_price")
    @Expose
    val discountedPrice: String? = null
    @SerializedName("original_price")
    @Expose
    val originalPrice: String? = null
    @SerializedName("remaining_stock_percentage")
    @Expose
    val remainingStockPercentage: Double = 0.toDouble()
    @SerializedName("stock_text")
    @Expose
    val stockText: String? = null

}
