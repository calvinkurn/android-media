package com.tokopedia.shop.settings.basicinfo.data


import com.google.gson.annotations.SerializedName


data class CheckShopIsPowerMerchantModel(
        @SerializedName("goldGetPMOSStatus")
        val goldGetPMOSStatus: GoldGetPMOSStatus = GoldGetPMOSStatus()
)

data class GoldGetPMOSStatus(
        @SerializedName("data")
        val `data`: DataPowerMerchant = DataPowerMerchant(),
        @SerializedName("header")
        val header: Header = Header()
)

data class DataPowerMerchant(
        @SerializedName("power_merchant")
        val powerMerchant: PowerMerchant = PowerMerchant()
)

data class PowerMerchant(
        @SerializedName("expired_time")
        val expiredTime: String = "",
        @SerializedName("status")
        val status: String = ""
)

data class Header(
        @SerializedName("messages")
        val messages: List<Any> = listOf(),
        @SerializedName("reason")
        val reason: String = ""
)