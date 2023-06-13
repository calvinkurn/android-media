package com.tokopedia.topchat.chatlist.domain.pojo.operational_insight

import com.google.gson.annotations.SerializedName

data class ShopChatMetricColorLight(
    @SerializedName("ChatRepliedIndicatorLight")
    var chatRepliedIndicatorLight: String? = "",

    @SerializedName("ChatSpeedIndicatorLight")
    var chatSpeedIndicatorLight: String? = "",

    @SerializedName("DiscussionRepliedIndicatorLight")
    var discussionRepliedIndicatorLight: String? = "",

    @SerializedName("DiscussionSpeedIndicatorLight")
    var discussionSpeedIndicatorLight: String? = "",
)
