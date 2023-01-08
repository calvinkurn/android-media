package com.tokopedia.topchat.chatlist.domain.pojo.operational_insight

import com.google.gson.annotations.SerializedName

data class ShopChatMetricColorDark(
    @SerializedName("ChatRepliedIndicatorDark")
    var chatRepliedIndicatorDark: String? = "",

    @SerializedName("ChatSpeedIndicatorDark")
    var chatSpeedIndicatorDark: String? = "",

    @SerializedName("DiscussionRepliedIndicatorDark")
    var discussionRepliedIndicatorDark: String? = "",

    @SerializedName("DiscussionSpeedIndicatorDark")
    var discussionSpeedIndicatorDark: String? = "",
)
