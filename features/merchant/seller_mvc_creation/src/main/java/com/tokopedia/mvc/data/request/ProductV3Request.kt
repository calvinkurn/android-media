package com.tokopedia.mvc.data.request

import com.google.gson.annotations.SerializedName

data class ProductV3ExtraInfo(
    @SerializedName("event") val event: Boolean,
)

data class ProductV3Options(
    @SerializedName("stats") val stats: Boolean,
    @SerializedName("txStats") val txStats: Boolean,
    @SerializedName("variant") val variant: Boolean,
    @SerializedName("basic") val basic: Boolean,
    @SerializedName("picture") val picture: Boolean,
)
