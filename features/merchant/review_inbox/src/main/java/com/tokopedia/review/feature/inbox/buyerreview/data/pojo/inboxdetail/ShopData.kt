package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopData {
    @SerializedName("shop_id")
    @Expose
    var shopId: Long = 0
        private set

    @SerializedName("shop_user_id")
    @Expose
    var shopUserId: Long = 0
        private set

    @SerializedName("domain")
    @Expose
    var domain: String? = null

    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null

    @SerializedName("shop_url")
    @Expose
    var shopUrl: String? = null

    @SerializedName("logo")
    @Expose
    var logo: String? = null

    @SerializedName("shop_reputation")
    @Expose
    var shopReputation: ShopReputation? = null
    fun setShopId(shopId: Int) {
        this.shopId = shopId.toLong()
    }

    fun setShopUserId(shopUserId: Int) {
        this.shopUserId = shopUserId.toLong()
    }
}