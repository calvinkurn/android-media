package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CouponUsesEntity(
    @Expose @SerializedName(value = "activeCountdown", alternate = ["active_count_down"])
    var activeCountDown: Long = 0,
    @Expose
    @SerializedName(value = "buttonUsage", alternate = ["btn_usage"])
    var btnUsage: CouponButtonUsageEntity = CouponButtonUsageEntity(),
    @Expose
    @SerializedName(value = "expiredCountdown", alternate = ["expired_count_down"])
    var expiredCountDown: Long = 0,
    @Expose
    @SerializedName("text")
    var text: String = "",
    @Expose
    @SerializedName(value = "usageStr", alternate = ["usage_str"])
    var usageStr: String = "",
)
