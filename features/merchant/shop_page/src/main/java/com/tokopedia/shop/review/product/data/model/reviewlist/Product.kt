package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product {
    @SerializedName("product_id")
    @Expose
    var productId = 0
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("product_image_url")
    @Expose
    var productImageUrl: String? = null
    @SerializedName("product_page_url")
    @Expose
    var productPageUrl: String? = null
    @SerializedName("product_price")
    @Expose
    var productPrice = 0

}