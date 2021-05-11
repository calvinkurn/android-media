package com.tokopedia.shop.common.graphql.data.shopoperationalhourslist

import com.google.gson.annotations.SerializedName

/**
 * Created by Rafli Syam on 16/04/2021
 */
data class ShopOperationalHoursListResponse(
        @SerializedName("getShopOperationalHours")
        val getShopOperationalHoursList: GetShopOperationalHoursList? = GetShopOperationalHoursList()
)

data class GetShopOperationalHoursList(
        @SerializedName("data")
        val data: List<ShopOperationalHour>? = listOf(),
        @SerializedName("error")
        val error: ShopOperationalHoursError? = ShopOperationalHoursError()
)

data class ShopOperationalHour(
        @SerializedName("day")
        val day: Int = 0,
        @SerializedName("startTime")
        var startTime: String = "",
        @SerializedName("endTime")
        var endTime: String = "",
        @SerializedName("status")
        val status: Int = 0
)

data class ShopOperationalHoursError(
        @SerializedName("message")
        val errorMessage: String = ""
)