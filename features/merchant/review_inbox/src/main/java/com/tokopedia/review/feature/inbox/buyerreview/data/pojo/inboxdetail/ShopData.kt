package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopData(
    @SerializedName("shop_id")
    @Expose
    val shopId: Long = 0,

    @SerializedName("shop_user_id")
    @Expose
    val shopUserId: Long = 0,

    @SerializedName("domain")
    @Expose
    val domain: String = "",

    @SerializedName("shop_name")
    @Expose
    val shopName: String = "",

    @SerializedName("shop_url")
    @Expose
    val shopUrl: String = "",

    @SerializedName("logo")
    @Expose
    val logo: String = "",

    @SerializedName("shop_reputation")
    @Expose
    val shopReputation: ShopReputation = ShopReputation()
)