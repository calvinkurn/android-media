package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName

data class MessageInfo(

    @field:SerializedName("detail")
    val detail: String = "",

    @field:SerializedName("message")
    val message: String = ""
)
