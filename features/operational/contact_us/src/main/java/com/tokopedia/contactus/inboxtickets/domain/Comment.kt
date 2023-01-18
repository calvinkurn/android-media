package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("create_by")
        val createBy: CreateBy? = null,
    @SerializedName("create_time")
        val createTime: String? = null,
    @SerializedName("attachment")
        var attachment: List<AttachmentItem>? = null,
    @SerializedName("message")
        var message: String? = null
)
