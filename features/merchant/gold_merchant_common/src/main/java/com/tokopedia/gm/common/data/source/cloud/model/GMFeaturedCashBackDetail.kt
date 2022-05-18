package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GMFeaturedCashBackDetail(
    @SerializedName("cashback_status")
    @Expose
    private val cashbackStatus: Long = 0,
    @SerializedName("cashback_percent")
    @Expose
    val cashbackPercent: Double = 0.0,
    @SerializedName("is_cashback_expired")
    @Expose
    private val isCashbackExpired: Long = 0,
    @SerializedName("cashback_value")
    @Expose
    private val cashbackValue: Long = 0
)