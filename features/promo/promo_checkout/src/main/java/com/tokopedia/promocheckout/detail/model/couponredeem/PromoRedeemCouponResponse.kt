package com.tokopedia.promocheckout.detail.model.couponredeem

import com.google.gson.annotations.SerializedName

data class PromoRedeemCouponResponse(

        @SerializedName("hachikoRedeem")
        val hachikoRedeem: HachikoRedeem? = null
)
