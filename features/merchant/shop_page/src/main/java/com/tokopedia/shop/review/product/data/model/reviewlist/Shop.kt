package com.tokopedia.shop.review.product.data.model.reviewlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Shop {
    @SerializedName("shop_id")
    @Expose
    var shopId = 0
    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

}