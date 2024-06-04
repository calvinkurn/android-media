package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.SerializedName

data class TopChatCampaign(
    @SerializedName("percentage")
    var percentage: Int? = 0,

    @SerializedName("label")
    var label: String? = "",

    @SerializedName("label_color")
    var labelColor: String? = ""
)
