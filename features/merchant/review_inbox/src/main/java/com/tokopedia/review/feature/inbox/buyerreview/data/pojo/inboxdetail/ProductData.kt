package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductData(
    @SerializedName("product_id")
    @Expose
    val productId: Long = 0,

    @SerializedName("product_name")
    @Expose
    val productName: String = "",

    @SerializedName("product_image_url")
    @Expose
    val productImageUrl: String = "",

    @SerializedName("product_page_url")
    @Expose
    val productPageUrl: String = "",

    @SerializedName("shop_id")
    @Expose
    val shopId: Long = 0,

    @SerializedName("product_status")
    @Expose
    val productStatus: Int = 0
)