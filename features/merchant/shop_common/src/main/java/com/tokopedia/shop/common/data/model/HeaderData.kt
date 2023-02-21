package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.SerializedName

data class HeaderData(
    @SerializedName("linkType")
    val linkType : String = "",
    @SerializedName("linkID")
    val linkID : Long = 0L
)
