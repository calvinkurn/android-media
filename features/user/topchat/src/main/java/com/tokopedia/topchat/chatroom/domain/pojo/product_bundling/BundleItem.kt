package com.tokopedia.topchat.chatroom.domain.pojo.product_bundling

import com.google.gson.annotations.SerializedName

data class BundleItem (
    @SerializedName("productID")
    var productId: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("image_url")
    var imageUrl: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("quantity")
    var quantity: String = ""
)