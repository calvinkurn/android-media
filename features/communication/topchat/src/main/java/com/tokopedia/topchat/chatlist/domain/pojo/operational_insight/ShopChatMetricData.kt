package com.tokopedia.topchat.chatlist.domain.pojo.operational_insight

import com.google.gson.annotations.SerializedName

data class ShopChatMetricData(
    @SerializedName("ChatReplied")
    var chatReplied: Float? = 0F,

    @SerializedName("ChatSpeed")
    var chatSpeed: Float? = 0F,

    @SerializedName("DiscussionReplied")
    var discussionReplied: Float? = 0F,

    @SerializedName("DiscussionSpeed")
    var discussionSpeed: Float? = 0F
)