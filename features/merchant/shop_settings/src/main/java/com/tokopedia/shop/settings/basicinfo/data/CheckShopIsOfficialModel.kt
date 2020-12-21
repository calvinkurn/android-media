package com.tokopedia.shop.settings.basicinfo.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class CheckShopIsOfficialModel(
        @SerializedName("getIsOfficial")
        @Expose
        val getIsOfficial: GetIsOfficial = GetIsOfficial()
)

data class GetIsOfficial(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data(),
        @SerializedName("message_error")
        @Expose
        val messageError: String = ""
)

data class Data(
        @SerializedName("expired_date")
        @Expose
        val expiredDate: String = "",
        @SerializedName("is_official")
        @Expose
        val isOfficial: Boolean = false
)