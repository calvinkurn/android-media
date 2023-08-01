package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class OriginData(
    @SerializedName("city_id")
    val cityId: Long = 0,
    @SerializedName("city_name")
    val cityName: String = ""
)
