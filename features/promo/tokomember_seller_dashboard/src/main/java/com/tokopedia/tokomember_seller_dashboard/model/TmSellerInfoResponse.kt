package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmSellerInfoResponse(
    @Expose
    @SerializedName("data")
    val sellerData: SellerData? = null
)

data class UserShopInfo(
    @Expose
    @SerializedName("info")
    val info: Info? = null
)

data class Info(
    @Expose
    @SerializedName("shop_id")
    val shopId: String? = null
)

data class SellerData(
    @Expose
    @SerializedName("userShopInfo")
    val userShopInfo: UserShopInfo? = null
)
