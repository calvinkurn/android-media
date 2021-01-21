package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RedeemCouponEntity (
    @Expose
    @SerializedName("coupons")
    var coupons: List<CouponDetailEntity>? = null ,

    @Expose
    @SerializedName("redeemMessage")
    var redeemMessage: String? = null

)