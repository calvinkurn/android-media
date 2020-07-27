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
    @SerializedName("shop_url")
    @Expose
    var shopUrl: String? = null
    @SerializedName("shop_logo")
    @Expose
    var shopLogo: String? = null
    @SerializedName("shop_reputation")
    @Expose
    var shopReputation: ShopReputation? = null

}