package com.tokopedia.shop.product.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CashbackDetail(
    @SerializedName("cashback_percent", alternate = ["cashback"])
    @Expose
    val cashbackPercent: Int = 0,

    @SerializedName("cashback_status")
    @Expose
    val cashbackStatus: Int = 0,

    @SerializedName("cashback_value", alternate = ["cashback_amount"])
    @Expose
    val cashbackValue: Int = 0,

    @SerializedName("is_cashback_expired")
    @Expose
    val isCashbackExpired: Int = 0
)
