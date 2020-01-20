package com.tokopedia.promocheckout.detail.model.couponredeem

import com.google.gson.annotations.SerializedName

data class HachikoRedeem(

        @SerializedName("coupons")
        val coupons: List<CouponsItem?>? = null,

        @SerializedName("reward_points")
        val rewardPoints: Int? = null
)
