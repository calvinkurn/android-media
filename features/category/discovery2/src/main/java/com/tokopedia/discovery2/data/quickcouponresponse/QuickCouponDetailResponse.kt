package com.tokopedia.discovery2.data.quickcouponresponse

import com.google.gson.annotations.SerializedName

data class QuickCouponDetailResponse(
        @SerializedName("GetOneClickCoupon")
        val clickCouponData: ClickCouponData?
)
