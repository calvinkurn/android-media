package com.tokopedia.shopdiscount.select.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING

data class SortRequest(
    @SerializedName("by")
    @Expose
    val by: String = EMPTY_STRING,
    @SerializedName("type")
    @Expose
    val type: String = EMPTY_STRING
)