package com.tokopedia.shopdiscount.select.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING

data class SortRequest(
    @SerializedName("by")
    val by: String = EMPTY_STRING,
    @SerializedName("type")
    val type: String = EMPTY_STRING
)