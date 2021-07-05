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
) {
    companion object {
        private const val OPERATIONAL_24_HOUR = "00:00:00 - 23:59:59"
    }

    fun is24Hour() = "$startTime - $endTime" == OPERATIONAL_24_HOUR
}

data class ShopOperationalHourError(
    @Expose
    @SerializedName("message")
    val message: String
)