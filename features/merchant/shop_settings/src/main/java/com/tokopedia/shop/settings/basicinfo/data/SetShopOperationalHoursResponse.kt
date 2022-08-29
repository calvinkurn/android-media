package com.tokopedia.shop.settings.basicinfo.data

import com.google.gson.annotations.SerializedName

data class SetShopOperationalHoursResponse(
        @SerializedName("setShopOperationalHours")
        val setShopOperationalHours: SetShopOperationalHours = SetShopOperationalHours()
)

data class SetShopOperationalHours(
        @SerializedName("success")
        val success: Boolean = false,
        @SerializedName("message")
        val message: String = "",
)