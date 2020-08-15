package com.tokopedia.shop.settings.basicinfo.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class CheckShopIsPowerMerchantModel(
        @SerializedName("power_merchant")
        @Expose
        val powerMerchant: PowerMerchant = PowerMerchant()
)

data class PowerMerchant(
        @SerializedName("expired_time")
        @Expose
        val expiredTime: String = "2020-08-24 00:00:00",
        @SerializedName("status")
        @Expose
        val status: String = "" // There are 3 status: active, "", idle
)