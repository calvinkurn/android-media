package com.tokopedia.topchat.chatroom.domain.pojo.param

import com.google.gson.annotations.SerializedName

data class ExistingMessageIdParam (
    @SerializedName("topShopId")
    var toShopId: String = "",

    @SerializedName("toUserId")
    var toUserId: String = "",

    @SerializedName("source")
    var source: String = ""
)