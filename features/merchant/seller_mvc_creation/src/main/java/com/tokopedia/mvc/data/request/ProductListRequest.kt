package com.tokopedia.mvc.data.request

import com.google.gson.annotations.SerializedName

data class GoodsFilterInput(
    @SerializedName("id") val id: String,
    @SerializedName("value") val value: List<String>
)

data class GoodsSortInput(
    @SerializedName("id") val id: String,
    @SerializedName("value") val value: String
)
