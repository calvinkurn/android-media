package com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 3/29/18.
 */

data class Product (

    @SerializedName("product_id")
    @Expose
    val productId: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("url_mobile")
    @Expose
    val urlMobile: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SerializedName("discount_percentage")
    @Expose
    val discountPercentage: Int = 0,
    @SerializedName("discounted_price")
    @Expose
    val discountedPrice: String = "",
    @SerializedName("original_price")
    @Expose
    val originalPrice: String = "",
    @SerializedName("remaining_stock_percentage")
    @Expose
    val remainingStockPercentage: Double = 0.toDouble(),
    @SerializedName("stock_text")
    @Expose
    val stockText: String = ""
){
}
