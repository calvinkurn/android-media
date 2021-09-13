package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductData {
    @SerializedName("product_id")
    @Expose
    var productId: Long = 0
        private set

    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @SerializedName("product_image_url")
    @Expose
    var productImageUrl: String? = null

    @SerializedName("product_page_url")
    @Expose
    var productPageUrl: String? = null

    @SerializedName("shop_id")
    @Expose
    var shopId = 0

    @SerializedName("product_status")
    @Expose
    var productStatus = 0
    fun setProductId(productId: Int) {
        this.productId = productId.toLong()
    }
}