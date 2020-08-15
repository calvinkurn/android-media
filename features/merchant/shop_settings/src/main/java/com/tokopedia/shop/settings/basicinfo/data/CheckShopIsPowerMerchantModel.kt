package com.tokopedia.shop.settings.basicinfo.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


//data class CheckShopIsPowerMerchantModel(
//        @SerializedName("power_merchant")
//        @Expose
//        val powerMerchant: PowerMerchant = PowerMerchant()
//)
//
////data class CheckShopIsOfficialModel(
////        @SerializedName("getIsOfficial")
////        @Expose
////        val getIsOfficial: GetIsOfficial = GetIsOfficial()
////)
////
////data class GetIsOfficial(
////        @SerializedName("data")
////        @Expose
////        val `data`: Data = Data(),
////        @SerializedName("message_error")
////        @Expose
////        val messageError: String = ""
////)

data class CheckShopIsPowerMerchantModel(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),
        @SerializedName("data")
        @Expose
        val `data`: DataPowerMerchant = DataPowerMerchant()
)

data class Header(
        @SerializedName("messages")
        @Expose
        val messages: String = "",
        @SerializedName("reason")
        @Expose
        val reason: String = ""
)

data class DataPowerMerchant(
        @SerializedName("power_merchant")
        @Expose
        val powerMerchant: PowerMerchant = PowerMerchant()
)

data class PowerMerchant(
        @SerializedName("expired_time")
        @Expose
        val expiredTime: String = "",
        @SerializedName("status")
        @Expose
        val status: String = "" // There are 3 status: active, "", idle
)