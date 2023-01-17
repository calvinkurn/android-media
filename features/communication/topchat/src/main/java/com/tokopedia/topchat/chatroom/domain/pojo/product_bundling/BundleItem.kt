package com.tokopedia.topchat.chatroom.domain.pojo.product_bundling

import com.google.gson.annotations.SerializedName

data class BundleItem (
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("image_url")
    var imageUrl: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("quantity")
    var quantity: String = "",
    @SerializedName("android_url")
    var androidUrl: String = "",
    @SerializedName("bundle_price_float")
    var price : Float = 0F
)