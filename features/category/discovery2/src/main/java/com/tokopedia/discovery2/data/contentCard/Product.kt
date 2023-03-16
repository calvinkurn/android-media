package com.tokopedia.discovery2.data.contentCard

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product_id")
    val productId: Long? = 0,

    @SerializedName("parent_id")
    val parentId: Long? = 0,

    @SerializedName("image_url_desktop")
    val imageUrlDesktop: String? = "",

    @SerializedName("image_url_mobile")
    val imageUrlMobile: String? = "",
)
