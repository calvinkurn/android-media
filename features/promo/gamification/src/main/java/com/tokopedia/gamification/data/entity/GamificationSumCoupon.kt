package com.tokopedia.gamification.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GamificationSumCoupon(
    @SerializedName("sumCoupon")
    @Expose
    var sumCoupon:Int = 0,

    @SerializedName("sumCouponStr")
@Expose
var sumCouponStr: String = "",
)