package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopOperationalHourResponse(
    @Expose
    @SerializedName("getShopOperationalHourStatus")
    val data: ShopOperationalHourResponse
)

data class ShopOperationalHourResponse(
    @Expose
    @SerializedName("timestamp")
    val timestamp: String,
    @Expose
    @SerializedName("statusActive")
    val statusActive: Boolean,
    @Expose
    @SerializedName("startTime")
    val startTime: String,
    @Expose
    @SerializedName("endTime")
    val endTime: String,
    @Expose
    @SerializedName("error")
    val error: ShopOperationalHourError?
)

data class ShopOperationalHourError(
    @Expose
    @SerializedName("message")
    val message: String
)