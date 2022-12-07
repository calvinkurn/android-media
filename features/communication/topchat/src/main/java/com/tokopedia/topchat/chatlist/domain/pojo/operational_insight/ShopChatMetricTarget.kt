package com.tokopedia.topchat.chatlist.domain.pojo.operational_insight

import com.google.gson.annotations.SerializedName

data class ShopChatMetricTarget(
    @SerializedName("ChatRepliedTarget")
    var chatRepliedTarget: Float? = 0F,

    @SerializedName("ChatSpeedTarget")
    var chatSpeedTarget: Float? = 0F,
)
