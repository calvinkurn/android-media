package com.tokopedia.discovery2.data.quickcouponresponse

import com.google.gson.annotations.SerializedName

data class ApplyCouponResponse (
        @SerializedName("save_cache_auto_apply")
        val applyCouponData: ApplyCouponData?
)