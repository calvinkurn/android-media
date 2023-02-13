package com.tokopedia.topchat.chatlist.domain.pojo.param

import com.google.gson.annotations.SerializedName

data class NotificationParam(
    @SerializedName("shop_id")
    val shopId: String
)